import commands.*;
import io.file.FileManager;
import io.file.JsonManager;
import io.ui.AskManager;
import managers.CommandManager;
import managers.collection.CollectionManager;
import managers.collection.StackManager;
import models.Flat;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AskManager askManager = new AskManager(scanner);
        CollectionManager collectionManager = new StackManager();
        CommandManager commandManager = new CommandManager();

        String filePath = System.getenv("LAB_FILE_PATH");
        if (filePath == null || filePath.isEmpty()) {
            System.out.println("ВНИМАНИЕ: Переменная LAB_FILE_PATH не задана. Используем default.json");
            filePath = "stack.json";
        }
        FileManager jsonManager = new JsonManager(filePath, collectionManager);

        jsonManager.read();

        commandManager.addCommand(new Help(commandManager));
        commandManager.addCommand(new Info(collectionManager));
        commandManager.addCommand(new Show(collectionManager));
        commandManager.addCommand(new Add(collectionManager));
        commandManager.addCommand(new Update(collectionManager));
        commandManager.addCommand(new RemoveById(collectionManager));
        commandManager.addCommand(new Clear(collectionManager));
        commandManager.addCommand(new Exit());
        commandManager.addCommand(new AddIfMin(collectionManager));
        commandManager.addCommand(new RemoveGreater(collectionManager));
        commandManager.addCommand(new Sort(collectionManager));
        commandManager.addCommand(new FilterByHouse(collectionManager));
        commandManager.addCommand(new PrintDescending(collectionManager));
        commandManager.addCommand(new PrintFieldDescendingNumberOfRooms(collectionManager));
        commandManager.addCommand(new Save(collectionManager, jsonManager));

        System.out.println("Программа запущена! Введите 'help' для просмотра доступных команд.");

        while (true) {
            System.out.print("\n> ");

            if (!scanner.hasNextLine()) {
                System.out.println("Поток ввода закрыт. Завершение программы.");
                break;
            }

            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String commandName = parts[0].toLowerCase();
            String argument = (parts.length > 1) ? parts[1].trim() : "";

            Flat flatArgument = null;

            if (commandName.equals("add") ||
                    commandName.equals("update") ||
                    commandName.equals("add_if_min") ||
                    commandName.equals("remove_greater")) {

                System.out.println("Для этой команды необходимо ввести данные объекта Flat:");
                try {
                    flatArgument = askManager.askFlat(); // Запускаем опрос пользователя
                } catch (Exception e) {
                    System.out.println("Отмена ввода или ошибка: " + e.getMessage());
                    continue;
                }
            }

            commandManager.execute(commandName, argument, flatArgument);
        }
    }
}