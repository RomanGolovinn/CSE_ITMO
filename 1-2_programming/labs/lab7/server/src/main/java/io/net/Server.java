package io.net;

import api.Request;
import api.Response;
import api.Serializer;
import io.db.UserManager;
import io.auth.UserContext;
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
    private final UserManager userManager;

    public Server(int port, CommandManager commandManager, UserManager userManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.serializer = new Serializer();
        this.userManager = userManager;
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

            String commandName = request.getCommandName();
            String username = request.getUsername();
            String password = request.getPassword();

            if ("register".equals(commandName)) {
                boolean isRegistered = userManager.registerUser(username, password);
                Response response = new Response(isRegistered, isRegistered ? "Регистрация успешна!" : "Ошибка: пользователь с таким логином уже существует.");
                sendResponse(response, clientAddress);
                return;
            }

            int userId = userManager.authenticateUser(username, password);
            if (userId == -1) {
                Response response = new Response(false, "Ошибка авторизации: неверный логин или пароль.");
                sendResponse(response, clientAddress);
                return;
            }

            UserContext.setId(userId);

            if ("login".equals(commandName)) {
                Response response = new Response(true, "Вход выполнен успешно!");
                sendResponse(response, clientAddress);
                return;
            }

            String resultText;
            boolean isSuccess = true;

            try {
                resultText = commandManager.execute(
                        request.getCommandName(),
                        request.getArgument(),
                        request.getFlatArgument()
                );
            } catch (Exception e) {
                isSuccess = false;
                resultText = "Ошибка при выполнении: " + e.getMessage();
            }

            if (resultText == null || resultText.trim().isEmpty()) {
                resultText = isSuccess ? "Команда выполнена успешно." : "Произошла неизвестная ошибка.";
            }

            Response response = new Response(isSuccess, resultText);
            sendResponse(response, clientAddress);

        } catch (Exception e) {
            logger.error("Ошибка десериализации или обработки запроса:", e);
        }
    }

    private void sendResponse(Response response, SocketAddress clientAddress) throws IOException {
        byte[] responseData = serializer.serialize(response);
        ByteBuffer responseBuffer = ByteBuffer.wrap(responseData);
        channel.send(responseBuffer, clientAddress);
        logger.info("Отправлен ответ клиенту");
    }
}