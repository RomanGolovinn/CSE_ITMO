package io.net;

import api.Request;
import api.Response;
import io.db.UserManager;
import io.auth.UserContext;
import managers.CommandManager;

import java.util.concurrent.Callable;

public class ClientHandler implements Callable<Response> {
    private final Request request;
    private final CommandManager commandManager;
    private final UserManager userManager;

    public ClientHandler(Request request, CommandManager commandManager, UserManager userManager) {
        this.request = request;
        this.commandManager = commandManager;
        this.userManager = userManager;
    }

    @Override
    public Response call() {
        String commandName = request.getCommandName();
        String username = request.getUsername();
        String password = request.getPassword();

        if ("register".equals(commandName)) {
            boolean isRegistered = userManager.registerUser(username, password);
            return new Response(isRegistered, isRegistered ? "Регистрация успешна!" : "Ошибка: пользователь с таким логином уже существует.");
        }

        int userId = userManager.authenticateUser(username, password);
        if (userId == -1) {
            return new Response(false, "Ошибка авторизации: неверный логин или пароль.");
        }

        UserContext.setId(userId);

        if ("login".equals(commandName)) {
            return new Response(true, "Вход выполнен успешно!");
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

        return new Response(isSuccess, resultText);
    }
}