package io.ui;

import managers.CommandManager;
import models.Flat;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console {
    private final CommandManager commandManager;
    private final AskManager askManager;

    public Console(CommandManager commandManager, AskManager askManager) {
        this.commandManager = commandManager;
        this.askManager = askManager;
    }

    public void start() {
        Scanner scanner = askManager.getScanner();
        System.out.println("Программа запущена! Введите 'help' для просмотра доступных команд.");

        while (true) {
            System.out.print("\n> ");

            try {
                if (!scanner.hasNextLine()) {
                    System.out.println("\nВвод закрыт. Завершение программы.");
                    break;
                }

                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0].toLowerCase();
                String arg = (parts.length > 1) ? parts[1].trim() : "";

                Flat flatArgument = null;

                if (commandName.equals("add") || commandName.equals("update") ||
                        commandName.equals("add_if_min") || commandName.equals("remove_greater")) {
                    try {
                        flatArgument = askManager.askFlat();
                    } catch (Exception e) {
                        System.out.println("Отмена ввода: " + e.getMessage());
                        continue;
                    }
                }

                commandManager.execute(commandName, arg, flatArgument);

            } catch (NoSuchElementException e) {
                System.out.println("\nЭкстренное завершение работы.");
                break;
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}