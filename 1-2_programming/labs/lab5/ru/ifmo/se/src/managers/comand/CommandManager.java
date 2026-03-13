package managers.comand;

import io.command.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final List<String> commandHistory = new ArrayList<>();

    public void register(Command command) {
        commands.put(command.getName(), command);
    }

    public void execute(String commandName, String argument) {
        Command command = commands.get(commandName);
        if (command == null) {
            System.out.println("Команда '" + commandName + "' не найдена. Наберите 'help' для справки.");
            return;
        }

        command.execute(argument);
        addToHistory(commandName);
    }

    private void addToHistory(String name) {
        commandHistory.add(name);
        if (commandHistory.size() > 11) {
            commandHistory.remove(0);
        }
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public List<String> getHistory() {
        return commandHistory;
    }
}
