package io.ui;

import managers.CommandManager;
import models.Flat;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Основной класс консольного интерфейса.
 * Обеспечивает работу интерактивного цикла: чтение строк из ввода,
 * их парсинг и передачу на выполнение в {@link CommandManager}.
 *
 * @author Roman Golovin
 */
public class Console {
    private final CommandManager commandManager;
    private final AskManager askManager;

    /**
     * Конструктор консоли.
     *
     * @param commandManager менеджер команд для выполнения введенных инструкций
     * @param askManager    менеджер опроса для получения сложных объектов (Flat)
     */
    public Console(CommandManager commandManager, AskManager askManager) {
        this.commandManager = commandManager;
        this.askManager = askManager;
    }

    /**
     * Запускает бесконечный цикл чтения команд из стандартного ввода.
     * Обрабатывает завершение ввода (Ctrl+D), разделяет имя команды и аргумент,
     * а также инициирует создание объекта Flat для соответствующих команд.
     */
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

                // Разделяем ввод на название команды и остальную строку (аргумент)
                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0].toLowerCase();
                String arg = (parts.length > 1) ? parts[1].trim() : "";

                Flat flatArgument = null;

                // Если команда требует объект Flat, вызываем AskManager
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