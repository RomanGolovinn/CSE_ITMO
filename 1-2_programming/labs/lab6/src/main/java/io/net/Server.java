package io.net;

import common.Request;
import common.Response;
import common.Serializer;
import managers.CommandManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Server {
    private final int port;
    private final CommandManager commandManager;
    private final Serializer serializer;
    private DatagramChannel channel;

    private static final Logger logger = LogManager.getLogger(Server.class);

    public Server(int port, CommandManager commandManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.serializer = new Serializer();
    }

    public void start() {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));

            System.out.println("Сервер запущен на порту " + port + ". Ожидание запросов...");

            ByteBuffer buffer = ByteBuffer.allocate(65535);

            while (true) {
                buffer.clear();
                SocketAddress clientAddress = channel.receive(buffer);

                if (clientAddress != null) {
                    processRequest(buffer, clientAddress);
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ignored) {}
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка сети на сервере: " + e.getMessage());
        }
    }

    private void processRequest(ByteBuffer buffer, SocketAddress clientAddress) {
        try {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            Request request = (Request) serializer.deserialize(data);
            logger.info("Получен запрос [{}] от {}", request.getCommandName(), clientAddress);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream trapStream = new PrintStream(baos, true, "UTF-8");
            PrintStream originalOut = System.out;

            boolean isSuccess = true;

            try {
                System.setOut(trapStream);
                commandManager.execute(request.getCommandName(), request.getArgument(), request.getFlatArgument());
                System.out.flush();
            } catch (Exception e) {
                isSuccess = false;
                System.out.println("Ошибка при выполнении: " + e.getMessage());
                System.out.flush();
            } finally {
                System.setOut(originalOut);
            }

            String resultText = baos.toString("UTF-8").trim();
            if (resultText.isEmpty() && isSuccess) {
                resultText = "Команда выполнена успешно.";
            }

            Response response = new Response(isSuccess, resultText);
            logger.info("Отправляем ответ клиенту: [{}]", response.getMessage());

            byte[] responseData = serializer.serialize(response);
            ByteBuffer responseBuffer = ByteBuffer.wrap(responseData);
            channel.send(responseBuffer, clientAddress);

        } catch (Exception e) {
            logger.error("Ошибка обработки запроса:", e);
        }
    }
}