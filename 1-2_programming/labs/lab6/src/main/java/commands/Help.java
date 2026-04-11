package commands;

import managers.CommandManager;
import models.Flat;

/**
 * Команда для вывода справки по доступным командам.
 * Отображает список всех зарегистрированных команд в приложении и их краткое описание.
 *
 * @author Roman Golovin
 */
public class Help implements Command {
    private final CommandManager commandManager;

    /**
     * Конструктор команды.
     *
     * @param commandManager менеджер команд, предоставляющий список всех доступных команд
     */
    public Help(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду вывода справки.
     * Перебирает все команды, зарегистрированные в менеджере, и выводит их в консоль.
     *
     * @param argument строковый аргумент (для данной команды не используется)
     * @param flat     объект квартиры (для данной команды не используется)
     */
    public void execute(String argument, Flat flat) {
        System.out.println("Список доступных команд:");

        for (Command command : commandManager.getCommands().values()) {
            System.out.println(command.getName() + ": " + command.getDescription());
        }
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("help")
     */
    public String getName() {
        return "help";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription() {
        return "вывести справку по доступным командам";
    }
}