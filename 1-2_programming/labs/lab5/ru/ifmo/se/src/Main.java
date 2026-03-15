import commands.*;
import io.AskManager;
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