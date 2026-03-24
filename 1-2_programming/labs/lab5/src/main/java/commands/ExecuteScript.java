package commands;

import io.ui.AskManager;
import managers.CommandManager;
import models.Flat;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Команда для выполнения скрипта из указанного файла.
 * Читает команды из файла построчно и выполняет их.
 * Включает встроенную защиту от бесконечной рекурсии (когда скрипт вызывает сам себя).
 *
 * @author Roman Golovin
 */
public class ExecuteScript implements Command {
    private final CommandManager commandManager;
    private final AskManager askManager;

    /**
     * Множество для отслеживания активных скриптов и предотвращения рекурсии.
     */
    private static final Set<String> activeScripts = new HashSet<>();

    /**
     * Конструктор команды.
     *
     * @param commandManager менеджер для выполнения считанных из скрипта команд
     * @param askManager     менеджер ввода для перенаправления сканера на чтение из файла
     */
    public ExecuteScript(CommandManager commandManager, AskManager askManager) {
        this.commandManager = commandManager;
        this.askManager = askManager;
    }

    /**
     * Выполняет чтение и запуск команд из файла скрипта.
     * Временно подменяет сканер в AskManager для чтения данных из файла,
     * а по завершении (или при ошибке) гарантированно возвращает старый сканер.
     *
     * @param argument путь к файлу скрипта
     * @param flat     объект квартиры (для данной команды не используется, передается null)
     */
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

        Scanner oldScanner = askManager.getScanner();

        try (Scanner scanner = new Scanner(file)) {
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
                        break;
                    }
                }

                commandManager.execute(commandName, arg, flatArgument);
            }

            askManager.setScanner(oldScanner);
            System.out.println("Выполнение скрипта " + argument + " завершено.");

        } catch (Exception e) {
            System.out.println("Ошибка при чтении скрипта: " + e.getMessage());
        } finally {
            askManager.setScanner(oldScanner);
            activeScripts.remove(absolutePath);
        }
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова ("execute_script")
     */
    @Override
    public String getName() {
        return "execute_script";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    @Override
    public String getDescription() {
        return "считать и исполнить скрипт из указанного файла";
    }
}