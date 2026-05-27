package io.net;

import api.Request;
import api.Response;
import api.Serializer;
import io.db.UserManager;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private final CommandManager commandManager;
    private final Serializer serializer;
    private DatagramChannel channel;
    private static final int BUFFER_SIZE = 65535;
    private static final Logger logger = LogManager.getLogger(Server.class);
    private final UserManager userManager;
    private final ExecutorService processingPool;
    private final ExecutorService sendingPool;

    public Server(int port, CommandManager commandManager, UserManager userManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.serializer = new Serializer();
        this.userManager = userManager;
        this.processingPool = Executors.newCachedThreadPool();
        this.sendingPool = Executors.newFixedThreadPool(10);
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

            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            processingPool.submit(() -> processRequestAsync(data, clientAddress));
        } catch (IOException e) {
            logger.error("Ошибка при получении пакета: {}", e.getMessage());
        }
    }

    private void processRequestAsync(byte[] data, SocketAddress clientAddress) {
        try {
            Request request = (Request) serializer.deserialize(data);
            ClientHandler handler = new ClientHandler(request, commandManager, userManager);
            Response response = handler.call();

            sendingPool.submit(() -> sendResponseAsync(response, clientAddress));
        } catch (Exception e) {
            logger.error("Ошибка обработки запроса: ", e);
        }
    }

    private void sendResponseAsync(Response response, SocketAddress clientAddress) {
        try {
            byte[] responseData = serializer.serialize(response);
            ByteBuffer responseBuffer = ByteBuffer.wrap(responseData);
            channel.send(responseBuffer, clientAddress);
        } catch (IOException e) {
            logger.error("Ошибка отправки ответа: {}", e.getMessage());
        }
    }
}