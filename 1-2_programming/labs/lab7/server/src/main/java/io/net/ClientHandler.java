package io.net;

import api.Request;
import api.Response;
import api.Serializer;
import io.db.UserManager;
import io.auth.UserContext;
import managers.CommandManager;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ClientHandler {
    private final DatagramChannel channel;
    private final Serializer serializer;
    private final CommandManager commandManager;
    private final UserManager userManager;

    private final ForkJoinPool readPool;
    private final ExecutorService processPool;

    public ClientHandler(DatagramChannel channel, CommandManager commandManager, UserManager userManager) {
        this.channel = channel;
        this.serializer = new Serializer();
        this.commandManager = commandManager;
        this.userManager = userManager;

        this.readPool = new ForkJoinPool();
        this.processPool = Executors.newFixedThreadPool(10);
    }

    public void handle(byte[] data, SocketAddress clientAddress) {
        readPool.submit(() -> {
            try {
                Request request = (Request) serializer.deserialize(data);

                processPool.submit(() -> {
                    Response response = null;
                    String commandName = request.getCommandName();

                    try {
                        if ("register".equals(commandName)) {
                            boolean success = userManager.register(request.getUsername(), request.getPassword());
                            response = new Response(success, success ? "Успешная регистрация" : "Логин занят");
                        } else {
                            int ownerId = userManager.authenticate(request.getUsername(), request.getPassword());

                            if (ownerId == -1) {
                                response = new Response(false, "Неверный логин или пароль!");
                            } else {
                                UserContext.setId(ownerId);

                                String resultText = commandManager.execute(commandName, request.getArgument(), request.getFlatArgument());
                                response = new Response(true, resultText);
                            }
                        }
                    } catch (Exception e) {
                        response = new Response(false, "Ошибка: " + e.getMessage());
                    } finally {
                        UserContext.clear();
                    }

                    Response finalResponse = response;
                    new Thread(() -> {
                        try {
                            byte[] responseData = serializer.serialize(finalResponse);
                            ByteBuffer buffer = ByteBuffer.wrap(responseData);
                            channel.send(buffer, clientAddress);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}