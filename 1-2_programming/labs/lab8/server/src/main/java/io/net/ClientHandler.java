package io.net;

import api.Request;
import api.Response;
import io.db.UserManager;
import io.db.FlatDatabaseManager;
import managers.collection.CollectionManager;
import io.auth.UserContext;
import managers.CommandManager;

import java.util.concurrent.Callable;

public class ClientHandler implements Callable<Response> {
    private final Request request;
    private final CommandManager commandManager;
    private final UserManager userManager;
    private final FlatDatabaseManager dbManager;
    private final CollectionManager collectionManager;

    public ClientHandler(Request request, CommandManager commandManager, UserManager userManager,
                         FlatDatabaseManager dbManager, CollectionManager collectionManager) {
        this.request = request;
        this.commandManager = commandManager;
        this.userManager = userManager;
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public Response call() {
        String commandName = request.getCommandName();
        String username = request.getUsername();
        String password = request.getPassword();

        if ("register".equals(commandName)) {
            boolean isRegistered = userManager.registerUser(username, password);
            return new Response(isRegistered, isRegistered ? "Регистрация успешна!" : "Ошибка регистрации.");
        }

        int userId = userManager.authenticateUser(username, password);
        if (userId == -1) {
            return new Response(false, "Ошибка авторизации.");
        }

        UserContext.setId(userId);

        if ("login".equals(commandName)) {
            return new Response(true, "Вход выполнен успешно!");
        }

        String resultText;
        boolean isSuccess = true;

        try {
            if ("add".equals(commandName)) {
                models.Flat flat = (models.Flat) request.getFlatArgument();
                Long generatedId = dbManager.addFlat(flat, userId);
                if (generatedId != -1L) {
                    flat.setId(generatedId);
                    flat.setOwnerId(userId);
                    collectionManager.add(flat);
                    resultText = "Квартира успешно добавлена с ID: " + generatedId;
                } else {
                    isSuccess = false;
                    resultText = "Ошибка сохранения в базу данных.";
                }
            } else if ("remove_by_id".equals(commandName)) {
                Long id = Long.parseLong(request.getArgument());
                boolean dbRemoved = dbManager.removeById(id, userId);
                if (dbRemoved) {
                    collectionManager.removeById(id);
                    resultText = "Квартира удалена.";
                } else {
                    isSuccess = false;
                    resultText = "Объект не найден или у вас нет прав.";
                }
            } else {
                resultText = commandManager.execute(commandName, request.getArgument(), request.getFlatArgument());
            }
        } catch (Exception e) {
            isSuccess = false;
            resultText = "Ошибка: " + e.getMessage();
        }

        Response response = new Response(isSuccess, resultText);

        if ("show".equals(commandName) && isSuccess) {
            response.setCollection(collectionManager.getCollection());
        }

        return response;
    }
}