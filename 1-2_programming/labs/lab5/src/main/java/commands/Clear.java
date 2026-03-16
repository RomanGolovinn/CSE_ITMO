package commands;

import main.java.managers.collection.CollectionManager;
import main.java.managers.models.Flat;

public class Clear implements Command {
    private final CollectionManager collection;

    public Clear(CollectionManager collection) {
        this.collection = collection;
    }

    public void execute(String argument, Flat flat) {
        collection.clear();
        System.out.println("Коллекция успешно очищена.");
    }

    public String getName() {
        return "clear";
    }

    public String getDescription() {
        return "очистить коллекцию";
    }
}