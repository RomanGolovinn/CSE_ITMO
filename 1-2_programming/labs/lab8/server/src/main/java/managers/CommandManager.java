package managers;

import commands.Command;
import models.Flat;

import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер команд.
 * Хранит реестр всех доступных команд в приложении и отвечает за их вызов по имени.
 * Позволяет динамически регистрировать новые команды и централизованно управлять их исполнением.
 *
 * @author Roman Golovin
 */
public class CommandManager {
    /** Карта зарегистрированных команд, где ключ — имя команды, значение — объект команды */
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Регистрирует новую команду в менеджере.
     *
     * @param command объект команды для добавления в реестр
     */
    public void register(Command command) {
        commands.put(command.getName(), command);
    }

    /**
     * Выполняет команду по её имени.
     * Если команда с таким именем не зарегистрирована, выводит соответствующее сообщение.
     *
     * @param commandName имя команды для поиска
     * @param argument    строковый аргумент, введенный пользователем
     * @param flat        объект квартиры, если он требуется для команды (иначе null)
     */
    public String execute(String commandName, String argument, Flat flat) {
        Command command = commands.get(commandName);
        if (command == null) {
            return ("Команда '" + commandName + "' не найдена. Наберите 'help' для справки.");
        }

        return command.execute(argument, flat);
    }

    /**
     * Дополнительный метод для добавления команды (аналог {@link #register(Command)}).
     *
     * @param command объект команды
     */
    public void addCommand(Command command){
        commands.put(command.getName(), command);
    }

    /**
     * Возвращает карту всех зарегистрированных команд.
     * Используется командой 'help' для формирования списка доступных функций.
     *
     * @return неизменяемая или текущая карта команд
     */
    public Map<String, Command> getCommands() {
        return commands;
    }
}