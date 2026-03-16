package main.java.managers;

import commands.Command;
import main.java.managers.models.Flat;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(Command command) {
        commands.put(command.getName(), command);
    }

    public void execute(String commandName, String argument, Flat flat) {
        Command command = commands.get(commandName);
        if (command == null) {
            System.out.println("Команда '" + commandName + "' не найдена. Наберите 'help' для справки.");
            return;
        }

        command.execute(argument, flat);
    }

    public void addCommand(Command command){
        commands.put(command.getName(), command);
    }


    public Map<String, Command> getCommands() {
        return commands;
    }
}
