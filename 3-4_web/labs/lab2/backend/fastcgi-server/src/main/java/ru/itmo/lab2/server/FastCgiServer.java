package ru.itmo.lab2.server;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class FastCgiServer {
    private static final int VERSION_1 = 1;
    private static final int BEGIN_REQUEST = 1;
    private static final int ABORT_REQUEST = 2;
    private static final int END_REQUEST = 3;
    private static final int PARAMS = 4;
    private static final int STDIN = 5;
    private static final int STDOUT = 6;
    private static final int MAX_BODY_BYTES = 1_048_576;

    private final String host;
    private final int port;
    private final FastCgiRequestHandler requestHandler;
    private final ExecutorService workers = Executors.newCachedThreadPool();

    public FastCgiServer(String host, int port, FastCgiRequestHandler requestHandler) {
        this.host = host;
        this.port = port;
        this.requestHandler = requestHandler;
    }

    public void start() throws IOException {
        InetAddress bindAddress = InetAddress.getByName(host);
        try (ServerSocket serverSocket = new ServerSocket(port, 50, bindAddress)) {
            System.out.println("FastCGI server listening on " + host + ":" + port);
            while (true) {
                Socket socket = serverSocket.accept();
                workers.submit(() -> handleConnection(socket));
            }
        }
    }

    private void handleConnection(Socket socket) {
        try (Socket client = socket) {
            DataInputStream input = new DataInputStream(client.getInputStream());
            OutputStream output = client.getOutputStream();

            while (true) {
                FastCgiRecord begin = readRecord(input);
                if (begin == null) {
                    return;
                }
                if (begin.type != BEGIN_REQUEST || begin.content.length < 3) {
                    continue;
                }

                int requestId = begin.requestId;
                boolean keepConnection = (begin.content[2] & 1) != 0;
                RequestData requestData = readRequestData(input, requestId);
                if (requestData == null) {
                    return;
                }

                HandlerResponse response;
                try {
                    response = requestHandler.handle(
                            requestData.environment,
                            new String(requestData.body, StandardCharsets.UTF_8)
                    );
                } catch (RuntimeException exception) {
                    response = new HandlerResponse(500,
                            "{\"error\":\"Internal server error\",\"results\":[]}");
                    System.err.println("FastCGI request failed: " + exception.getMessage());
                }

                writeResponse(output, requestId, response);
                if (!keepConnection) {
                    return;
                }
            }
        } catch (IOException exception) {
            System.err.println("FastCGI connection ended: " + exception.getMessage());
        }
    }

    private RequestData readRequestData(DataInputStream input, int requestId) throws IOException {
        ByteArrayOutputStream params = new ByteArrayOutputStream();
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        boolean stdinFinished = false;

        while (!stdinFinished) {
            FastCgiRecord record = readRecord(input);
            if (record == null) {
                return null;
            }
            if (record.requestId != requestId) {
                continue;
            }

            if (record.type == ABORT_REQUEST) {
                return new RequestData(new HashMap<>(), new byte[0]);
            }
            if (record.type == PARAMS && record.content.length > 0) {
                appendLimited(params, record.content, MAX_BODY_BYTES);
            } else if (record.type == STDIN) {
                if (record.content.length == 0) {
                    stdinFinished = true;
                } else {
                    appendLimited(body, record.content, MAX_BODY_BYTES);
                }
            }
        }

        return new RequestData(parseParams(params.toByteArray()), body.toByteArray());
    }

    private void appendLimited(ByteArrayOutputStream target, byte[] bytes, int limit) throws IOException {
        if (target.size() + bytes.length > limit) {
            throw new IOException("FastCGI request exceeds the " + limit + " byte limit");
        }
        target.write(bytes);
    }

    private Map<String, String> parseParams(byte[] encoded) throws IOException {
        Map<String, String> params = new HashMap<>();
        int offset = 0;
        while (offset < encoded.length) {
            Length nameLength = readLength(encoded, offset);
            offset = nameLength.nextOffset;
            Length valueLength = readLength(encoded, offset);
            offset = valueLength.nextOffset;

            long pairEnd = (long) offset + nameLength.value + valueLength.value;
            if (pairEnd > encoded.length) {
                throw new IOException("Malformed FastCGI PARAMS record");
            }

            String name = new String(encoded, offset, nameLength.value, StandardCharsets.UTF_8);
            offset += nameLength.value;
            String value = new String(encoded, offset, valueLength.value, StandardCharsets.UTF_8);
            offset += valueLength.value;
            params.put(name, value);
        }
        return params;
    }

    private Length readLength(byte[] data, int offset) throws IOException {
        if (offset >= data.length) {
            throw new IOException("Malformed FastCGI parameter length");
        }
        int first = data[offset] & 0xff;
        if ((first & 0x80) == 0) {
            return new Length(first, offset + 1);
        }
        if (offset + 4 > data.length) {
            throw new IOException("Malformed FastCGI parameter length");
        }
        int length = ((first & 0x7f) << 24)
                | ((data[offset + 1] & 0xff) << 16)
                | ((data[offset + 2] & 0xff) << 8)
                | (data[offset + 3] & 0xff);
        return new Length(length, offset + 4);
    }

    private FastCgiRecord readRecord(DataInputStream input) throws IOException {
        int version = input.read();
        if (version < 0) {
            return null;
        }
        byte[] header = new byte[7];
        input.readFully(header);
        if (version != VERSION_1) {
            throw new IOException("Unsupported FastCGI version: " + version);
        }

        int type = header[0] & 0xff;
        int requestId = ((header[1] & 0xff) << 8) | (header[2] & 0xff);
        int contentLength = ((header[3] & 0xff) << 8) | (header[4] & 0xff);
        int paddingLength = header[5] & 0xff;
        byte[] content = new byte[contentLength];
        input.readFully(content);
        if (paddingLength > 0) {
            input.skipNBytes(paddingLength);
        }
        return new FastCgiRecord(type, requestId, content);
    }

    private void writeResponse(OutputStream output, int requestId, HandlerResponse response) throws IOException {
        String headers = "Status: " + response.getStatus() + " " + reasonPhrase(response.getStatus()) + "\r\n"
                + "Content-Type: application/json; charset=UTF-8\r\n"
                + "Cache-Control: no-store\r\n\r\n";
        writeRecord(output, STDOUT, requestId, headers.getBytes(StandardCharsets.UTF_8));
        writeRecord(output, STDOUT, requestId, response.getBody().getBytes(StandardCharsets.UTF_8));
        writeRecord(output, STDOUT, requestId, new byte[0]);
        writeRecord(output, END_REQUEST, requestId, new byte[8]);
        output.flush();
    }

    private void writeRecord(OutputStream output, int type, int requestId, byte[] content) throws IOException {
        if (content.length > 65_535) {
            throw new IOException("FastCGI response record is too large");
        }
        output.write(VERSION_1);
        output.write(type);
        output.write((requestId >>> 8) & 0xff);
        output.write(requestId & 0xff);
        output.write((content.length >>> 8) & 0xff);
        output.write(content.length & 0xff);
        output.write(0);
        output.write(0);
        output.write(content);
    }

    private String reasonPhrase(int status) {
        switch (status) {
            case 200:
                return "OK";
            case 400:
                return "Bad Request";
            case 405:
                return "Method Not Allowed";
            case 415:
                return "Unsupported Media Type";
            default:
                return "Internal Server Error";
        }
    }

    private static final class FastCgiRecord {
        private final int type;
        private final int requestId;
        private final byte[] content;

        private FastCgiRecord(int type, int requestId, byte[] content) {
            this.type = type;
            this.requestId = requestId;
            this.content = content;
        }
    }

    private static final class RequestData {
        private final Map<String, String> environment;
        private final byte[] body;

        private RequestData(Map<String, String> environment, byte[] body) {
            this.environment = environment;
            this.body = body;
        }
    }

    private static final class Length {
        private final int value;
        private final int nextOffset;

        private Length(int value, int nextOffset) {
            this.value = value;
            this.nextOffset = nextOffset;
        }
    }
}
