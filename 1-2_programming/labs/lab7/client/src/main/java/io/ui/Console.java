package io.ui;

import api.Response;
import io.net.Client;
import models.Flat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class Console {
    private final Client client;
    private final AskManager askManager;
    private final Set<String> scriptHistory = new HashSet<>();

    public Console(Client client, AskManager askManager) {
        this.client = client;
        this.askManager = askManager;
    }

    public boolean authorize() {
        Scanner scanner = askManager.getScanner();
        System.out.println("Добро пожаловать в систему управления коллекцией!");

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1 - Войти");
            System.out.println("2 - Зарегистрироваться");
            System.out.println("3 - Выход");
            System.out.print("> ");

            if (!scanner.hasNextLine()) return false;
            String choice = scanner.nextLine().trim();

            if (choice.equals("3") || choice.equals("exit")) {
                System.out.println("Завершение работы клиента.");
                return false;
            }

            if (!choice.equals("1") && !choice.equals("2")) {
                System.out.println("Ошибка: неверный ввод.");
                continue;
            }

            System.out.print("Логин: ");
            String login = scanner.nextLine().trim();
            System.out.print("Пароль: ");
            String password = scanner.nextLine().trim();

            if (login.isEmpty() || password.isEmpty()) {
                System.out.println("Ошибка: логин и пароль не могут быть пустыми.");
                continue;
            }

            client.setCredentials(login, password);

            String commandToSend = choice.equals("2") ? "register" : "help";

            try {
                Response response = client.sendCommand(commandToSend, "", null);
                if (response != null && response.isSuccess()) {
                    System.out.println(choice.equals("2") ? "Регистрация успешна!" : "Вход выполнен успешно!");
                    return true;
                } else {
                    System.out.println("Ошибка: " + (response != null ? response.getMessage() : "нет ответа от сервера"));
                    client.setCredentials(null, null);
                }
            } catch (Exception e) {
                System.out.println("Сетевая ошибка при обращении к серверу: " + e.getMessage());
                client.setCredentials(null, null);
            }
        }
    }

    private void processCommand(String commandName, String argument, Flat flatArgument) {
        try {
            Response response = client.sendCommand(commandName, argument, flatArgument);
            if (response != null && response.getMessage() != null) {
                System.out.println(response.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении: " + e.getMessage());
        }
    }

    public void start() {
        System.out.println("Введите 'help' для просмотра доступных команд.");
        try {
            while (true) {
                System.out.print("\n> ");

                Scanner currentScanner = askManager.getScanner();
                if (!currentScanner.hasNextLine()) break;

                String line = currentScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] input = line.split("\\s+", 2);
                String commandName = input[0];
                String argument = input.length > 1 ? input[1] : "";

                if (commandName.equals("exit")) {
                    System.out.println("Завершение работы клиента.");
                    break;
                }

                if (commandName.equals("execute_script")) {
                    runScript(argument);
                    continue;
                }

                Flat flatArgument = null;
                if (commandName.equals("add") || commandName.equals("add_if_min") || commandName.equals("update")) {
                    flatArgument = askManager.askFlat();
                }

                processCommand(commandName, argument, flatArgument);
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nЭкстренное завершение работы.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void runScript(String fileName) {
        this.askManager.setInteractive(false);
        if (!scriptHistory.add(fileName)) {
            System.out.println("Рекурсия: " + fileName);
            return;
        }

        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            Scanner oldScanner = askManager.getScanner();
            askManager.setScanner(fileScanner);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] input = line.split("\\s+", 2);
                String commandName = input[0];
                String argument = input.length > 1 ? input[1] : "";

                if (commandName.equals("execute_script")) {
                    runScript(argument);
                    continue;
                }

                Flat flatArgument = null;
                if (commandName.equals("add") || commandName.equals("add_if_min") || commandName.equals("update")) {
                    flatArgument = askManager.askFlat();
                }

                processCommand(commandName, argument, flatArgument);
            }

            askManager.setScanner(oldScanner);

        } catch (FileNotFoundException e) {
            System.out.println("Файл скрипта не найден: " + fileName);
        } catch (NoSuchElementException e) {
            System.out.println("Некорректные данные в скрипте.");
        } finally {
            scriptHistory.remove(fileName);
        }

        this.askManager.setInteractive(true);
    }
}