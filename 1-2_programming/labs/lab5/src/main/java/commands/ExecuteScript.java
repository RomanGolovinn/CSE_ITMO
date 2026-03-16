package commands;

import io.ui.AskManager;
import managers.CommandManager;
import models.Flat;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ExecuteScript implements Command {
    private final CommandManager commandManager;
    private final AskManager askManager;

    private static final Set<String> activeScripts = new HashSet<>();

    public ExecuteScript(CommandManager commandManager, AskManager askManager) {
        this.commandManager = commandManager;
        this.askManager = askManager;
    }

    @Override
    public void execute(String argument, Flat flat) {
        File file = new File(argument);
        if (!file.exists()) {
            System.out.println("Скрипт не найден: " + argument);
            return;
        }

        String absolutePath = file.getAbsolutePath();

        if (activeScripts.contains(absolutePath)) {
            System.out.println("Ошибка: обнаружена рекурсия! Скрипт " + argument + " пытается вызвать сам себя.");
            return;
        }

        activeScripts.add(absolutePath);

        try (Scanner scanner = new Scanner(file)) {
            Scanner oldScanner = askManager.getScanner();
            askManager.setScanner(scanner);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0].toLowerCase();
                String arg = (parts.length > 1) ? parts[1].trim() : "";

                Flat flatArgument = null;

                if (commandName.equals("add") ||
                        commandName.equals("update") ||
                        commandName.equals("add_if_min") ||
                        commandName.equals("remove_greater")) {

                    try {
                        flatArgument = askManager.askFlat();
                    } catch (Exception e) {
                        System.out.println("Отмена ввода или ошибка в скрипте: " + e.getMessage());
                        continue;
                    }
                }

                commandManager.execute(commandName, arg, flatArgument);
            }

            askManager.setScanner(oldScanner);
            System.out.println("Выполнение скрипта " + argument + " завершено.");

        } catch (Exception e) {
            System.out.println("Ошибка при чтении скрипта: " + e.getMessage());
        } finally {

            activeScripts.remove(absolutePath);
        }
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getDescription() {
        return "считать и исполнить скрипт из указанного файла";
    }
}