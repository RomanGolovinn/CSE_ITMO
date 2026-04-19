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
        System.out.println("Программа запущена! Введите 'help' для просмотра доступных команд.");
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
    }
}