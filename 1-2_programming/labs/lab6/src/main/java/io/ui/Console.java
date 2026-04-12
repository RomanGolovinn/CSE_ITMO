package io.ui;

import io.net.Client;
import models.Flat;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Основной класс консольного интерфейса.
 * Обеспечивает работу интерактивного цикла: чтение строк из ввода,
 * их парсинг и передачу на выполнение в {@link Client}.
 *
 * @author Roman Golovin
 */
public class Console {
    private final Client client;
    private final AskManager askManager;

    /**
     * Конструктор консоли.
     *
     * @param client менеджер для отправки введёных инструкций на сервер инструкций
     * @param askManager    менеджер опроса для получения сложных объектов (Flat)
     */
    public Console(Client client, AskManager askManager) {
        this.client = client;
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

                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0].toLowerCase();
                String arg = (parts.length > 1) ? parts[1].trim() : "";

                Flat flatArgument = null;

                if (commandName.equals("update")) {
                    if (arg.isEmpty()) {
                        System.out.println("Ошибка: Команда update требует аргумент (ID). Пример: update 5");
                        continue;
                    }
                    try {
                        Long.parseLong(arg);
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: ID должен быть целым числом!");
                        continue;
                    }
                }

                if (commandName.equals("add") || commandName.equals("update") ||
                        commandName.equals("add_if_min") || commandName.equals("remove_greater")) {
                    try {
                        flatArgument = askManager.askFlat();
                    } catch (Exception e) {
                        System.out.println("Отмена ввода: " + e.getMessage());
                        continue;
                    }
                }

                client.sendCommand(commandName, arg, flatArgument);

            } catch (NoSuchElementException e) {
                System.out.println("\nЭкстренное завершение работы.");
                break;
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}