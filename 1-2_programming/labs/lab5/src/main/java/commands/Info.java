package commands;

import main.java.managers.collection.CollectionManager;
import main.java.managers.models.Flat;

public class Info implements Command {
    private final CollectionManager collection;

    public Info(CollectionManager collection) {
        this.collection = collection;
    }

    public void execute(String argument, Flat flat) {
        System.out.println("Информация о коллекции:");
        System.out.println(collection.getInfo());
    }

    public String getName() {
        return "info";
    }

    public String getDescription() {
        return "вывести в стандартный поток вывода информацию о коллекции";
    }
}