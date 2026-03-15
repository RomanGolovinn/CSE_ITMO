package commands;

import managers.CommandManager;
import models.Flat;

public class Help implements Command {
    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void execute(String argument, Flat flat) {
        System.out.println("Список доступных команд:");

        for (Command command : commandManager.getCommands().values()) {
            System.out.println(command.getName() + ": " + command.getDescription());
        }
    }

    public String getName() {
        return "help";
    }

    public String getDescription() {
        return "вывести справку по доступным командам";
    }
}
