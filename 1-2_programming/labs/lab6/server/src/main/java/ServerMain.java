import commands.*;
import io.file.FileManager;
import io.file.JsonManager;
import io.github.cdimascio.dotenv.Dotenv;
import io.net.Server;
import managers.CommandManager;
import managers.collection.CollectionManager;
import managers.collection.StackManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerMain {
    private static final Logger logger = LogManager.getLogger(ServerMain.class);

    public static void main(String[] args) {
        logger.info("Инициализация сервера...");

        Dotenv dotenv = Dotenv.load();
        int port = Integer.parseInt(dotenv.get("SERVER_PORT"));

        try {
            CollectionManager collectionManager = new StackManager();
            CommandManager commandManager = new CommandManager();

            String filePath = System.getenv("LAB_FILE_PATH");
            if (filePath == null || filePath.isEmpty()) {
                logger.warn("ВНИМАНИЕ: Переменная LAB_FILE_PATH не задана. Используем default.json");
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
            commandManager.addCommand(new Exit(commandManager));
            commandManager.addCommand(new AddIfMin(collectionManager));
            commandManager.addCommand(new RemoveGreater(collectionManager));
            commandManager.addCommand(new Sort(collectionManager));
            commandManager.addCommand(new FilterByHouse(collectionManager));
            commandManager.addCommand(new PrintDescending(collectionManager));
            commandManager.addCommand(new PrintFieldDescendingNumberOfRooms(collectionManager));
            commandManager.addCommand(new Save(collectionManager, jsonManager));
            commandManager.addCommand(new Begin(collectionManager));
            commandManager.addCommand(new Commit(collectionManager));
            commandManager.addCommand(new RollBack(collectionManager));

            logger.info("Коллекция успешно загружена. Элементов: " + collectionManager.getCollection().size());

            Server server = new Server(port, commandManager);
            logger.info("Сервер готов к приему пакетов на порту " + port);

            server.start();

        } catch (Exception e) {
            logger.fatal("Критическая ошибка сервера: ", e);
        }
    }
}