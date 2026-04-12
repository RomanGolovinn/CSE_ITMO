import commands.*;
import io.file.FileManager;
import io.file.JsonManager;
import io.net.Client;
import io.ui.AskManager;
import io.ui.Console;
import managers.CommandManager;
import managers.collection.CollectionManager;
import managers.collection.StackManager;

import java.util.Scanner;

/**
 * Главный класс приложения.
 * Отвечает за инициализацию всех компонентов системы: менеджеров, обработчиков ввода-вывода
 * и регистрацию команд. Управляет жизненным циклом программы.
 *
 * @author Roman Golovin
 */
public class Main {
    /**
     * Точка входа в программу.
     * Выполняет последовательную настройку окружения, загрузку данных из файла
     * и запуск интерактивной консоли.
     *
     * @param args аргументы командной строки (не используются)
     */
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
        commandManager.addCommand(new ExecuteScript(commandManager, askManager));
        commandManager.addCommand(new Begin(collectionManager));
        commandManager.addCommand(new Commit(collectionManager));
        commandManager.addCommand(new RollBack(collectionManager));

        Console console = new Console(new Client(), askManager);
        console.start();
    }
}