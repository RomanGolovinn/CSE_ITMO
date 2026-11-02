package ru.itmo.lab2.server;

import com.fastcgi.FCGIInterface;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class FastCgiServer {
    private static final int MAX_BODY_BYTES = 1_048_576;
    private static final String INVALID_BODY_RESPONSE = "{\"error\":\"Invalid request body\",\"results\":[]}";

    private final FastCgiRequestHandler requestHandler;

    public FastCgiServer(FastCgiRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public void start() {
        FCGIInterface fastCgi = new FCGIInterface();
        while (fastCgi.FCGIaccept() >= 0) {
            HandlerResponse response;
            try {
                Properties parameters = FCGIInterface.request.params;
                byte[] body = readBody(parameters);
                response = requestHandler.handle(
                        toMap(parameters),
                        new String(body, StandardCharsets.UTF_8)
                );
            } catch (IllegalArgumentException exception) {
                response = new HandlerResponse(400, INVALID_BODY_RESPONSE);
            } catch (IOException exception) {
                System.err.println("Could not read FastCGI request: " + exception.getMessage());
                response = new HandlerResponse(400, INVALID_BODY_RESPONSE);
            } catch (RuntimeException exception) {
                System.err.println("FastCGI request failed: " + exception.getMessage());
                response = new HandlerResponse(500,
                        "{\"error\":\"Internal server error\",\"results\":[]}");
            }

            try {
                writeResponse(FCGIInterface.request.outStream, response);
            } catch (IOException exception) {
                System.err.println("Could not write FastCGI response: " + exception.getMessage());
            }
        }
    }

    private byte[] readBody(Properties parameters) throws IOException {
        String rawLength = parameters.getProperty("CONTENT_LENGTH", "0");
        final int contentLength;
        try {
            contentLength = Integer.parseInt(rawLength);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid Content-Length", exception);
        }
        if (contentLength < 0 || contentLength > MAX_BODY_BYTES) {
            throw new IllegalArgumentException("Request body is too large");
        }

        byte[] body = System.in.readNBytes(contentLength);
        if (body.length != contentLength) {
            throw new IOException("Unexpected end of request body");
        }
        return body;
    }

    private Map<String, String> toMap(Properties parameters) {
        Map<String, String> values = new HashMap<>();
        for (String name : parameters.stringPropertyNames()) {
            values.put(name, parameters.getProperty(name));
        }
        return values;
    }

    private void writeResponse(java.io.OutputStream output, HandlerResponse response) throws IOException {
        String headers = "Status: " + response.getStatus() + " " + reasonPhrase(response.getStatus()) + "\r\n"
                + "Content-Type: application/json; charset=UTF-8\r\n"
                + "Cache-Control: no-store\r\n\r\n";
        output.write(headers.getBytes(StandardCharsets.UTF_8));
        output.write(response.getBody().getBytes(StandardCharsets.UTF_8));
        output.flush();
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
}
