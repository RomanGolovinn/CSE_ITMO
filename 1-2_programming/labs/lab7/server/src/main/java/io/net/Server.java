package io.net;

import api.Request;
import api.Response;
import api.Serializer;
import io.file.FileManager;
import managers.CommandManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class Server {
    private final int port;
    private final CommandManager commandManager;
    private final Serializer serializer;
    private DatagramChannel channel;
    private static final int BUFFER_SIZE = 65535;
    private static final Logger logger = LogManager.getLogger(Server.class);
    private FileManager fileManager;

    public Server(int port, CommandManager commandManager, FileManager fileManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.serializer = new Serializer();
        this.fileManager = fileManager;
    }

    public void start() {
        try (Selector selector = Selector.open()) {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));

            channel.register(selector, SelectionKey.OP_READ);

            logger.info("Сервер запущен на порту {}. Ожидание запросов...", port);

            while (true) {
                if (selector.select() == 0) continue;

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isReadable()) {
                        handleRead();
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка сети на сервере: {}", e.getMessage());
        }
    }

    private void handleRead() {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            SocketAddress clientAddress = channel.receive(buffer);
            if (clientAddress == null) return;

            processRequest(buffer, clientAddress);
        } catch (IOException e) {
            logger.error("Ошибка при получении пакета: {}", e.getMessage());
        }
    }

    private void processRequest(ByteBuffer buffer, SocketAddress clientAddress) {
        try {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            Request request = (Request) serializer.deserialize(data);
            logger.info("Получен запрос [{}] от {}", request.getCommandName(), clientAddress);

            String resultText;
            boolean isSuccess = true;

            try {
                fileManager.read();
                resultText = commandManager.execute(
                        request.getCommandName(),
                        request.getArgument(),
                        request.getFlatArgument()
                );
                fileManager.save();
            } catch (Exception e) {
                isSuccess = false;
                resultText = "Ошибка при выполнении: " + e.getMessage();
            }

            if (resultText == null || resultText.trim().isEmpty()) {
                resultText = isSuccess ? "Команда выполнена успешно." : "Произошла неизвестная ошибка.";
            }

            Response response = new Response(isSuccess, resultText);
            logger.info("Отправляем ответ клиенту ({} байт)", resultText.length());

            byte[] responseData = serializer.serialize(response);
            ByteBuffer responseBuffer = ByteBuffer.wrap(responseData);
            channel.send(responseBuffer, clientAddress);

        } catch (Exception e) {
            logger.error("Ошибка десериализации или обработки запроса:", e);
        }
    }
}