import commands.*;
import io.db.DatabaseConnectionManager;
import io.db.FlatDatabaseManager;
import io.db.UserManager;
import io.github.cdimascio.dotenv.Dotenv;
import io.net.Server;
import managers.CommandManager;
import managers.collection.CollectionManager;
import managers.collection.StackManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerMain {
    private static final Logger logger = LogManager.getLogger(ServerMain.class);
    private static UserManager userManager;

    public static void main(String[] args) {
        logger.info("Инициализация сервера...");

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String portString = dotenv.get("PORT");
        int port = (portString != null && !portString.isEmpty()) ? Integer.parseInt(portString) : 48496;
        String dbUrl = dotenv.get("DB_URL");
        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");

        if (dbUrl == null || dbUser == null || dbPassword == null) {
            logger.fatal("Не заданы переменные окружения БД (DB_URL, DB_USER, DB_PASSWORD) в файле .env");
            System.exit(1);
        }

        try {
            DatabaseConnectionManager connectionManager = new DatabaseConnectionManager(dbUrl, dbUser, dbPassword);

            connectionManager.migrate();

            userManager = new UserManager(connectionManager);
            FlatDatabaseManager flatDbManager = new FlatDatabaseManager(connectionManager);

            CollectionManager collectionManager = new StackManager();
            collectionManager.setDbManager(flatDbManager);
            collectionManager.loadFromDatabase();

            CommandManager commandManager = new CommandManager();

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
            commandManager.addCommand(new Begin(collectionManager));
            commandManager.addCommand(new Commit(collectionManager));
            commandManager.addCommand(new RollBack(collectionManager));

            logger.info("Коллекция успешно загружена из БД. Элементов: " + collectionManager.getCollection().size());

            Server server = new Server(port, commandManager, userManager, flatDbManager, collectionManager);

            server.start();

        } catch (Exception e) {
            logger.fatal("Критическая ошибка сервера: ", e);
        }
    }

    public static UserManager getUserManager() {
        return userManager;
    }
}