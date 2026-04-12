package io.ui;

import common.Response;
import io.net.Client;
import models.Flat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

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
    private final Set<String> activeScripts = new HashSet<>();

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

                if (commandName.equals("execute_script")){
                    runScript(arg);
                    continue;
                }

                Response response = client.sendCommand(commandName, arg, flatArgument);
                System.out.println(response.getMessage());

            } catch (NoSuchElementException e) {
                System.out.println("\nЭкстренное завершение работы.");
                break;
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void runScript(String fileName) {
        if (activeScripts.contains(fileName)) {
            System.out.println("Ошибка: обнаружена бесконечная рекурсия в скрипте " + fileName);
            return;
        }

        File scriptFile = new File(fileName);
        try (Scanner scriptScanner = new Scanner(scriptFile)) {
            activeScripts.add(fileName);
            System.out.println("Начинаем выполнение скрипта: " + fileName);

            AskManager fileAskManager = new AskManager(scriptScanner);

            while (scriptScanner.hasNextLine()) {
                String input = scriptScanner.nextLine().trim();
                if (input.isEmpty()) continue;

                System.out.println("Выполнение команды из скрипта: " + input);

                String[] tokens = input.split(" ", 2);
                String scriptCommand = tokens[0];
                String scriptArgument = tokens.length > 1 ? tokens[1] : "";

                Flat flatArgument = null;

                if (scriptCommand.equals("add") || scriptCommand.equals("update") || scriptCommand.equals("add_if_min")) {
                    try {
                        flatArgument = fileAskManager.askFlat();
                    } catch (Exception e) {
                        System.out.println("Ошибка чтения данных квартиры из скрипта. Выполнение скрипта прервано.");
                        break;
                    }
                }

                if (scriptCommand.equals("execute_script")) {
                    runScript(scriptArgument);
                    continue;
                }

                try {
                    Response response = client.sendCommand(scriptCommand, scriptArgument, flatArgument);
                    System.out.println("Ответ сервера: " + response.getMessage());
                } catch (Exception e) {
                    System.out.println("Ошибка сети при выполнении скрипта: Сервер недоступен.");
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: файл скрипта не найден (" + fileName + ")");
        } finally {
            activeScripts.remove(fileName);
        }
    }
}