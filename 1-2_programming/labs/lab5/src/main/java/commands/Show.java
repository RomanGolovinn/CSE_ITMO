package commands;

import managers.collection.CollectionManager;
import models.Flat;

public class Show implements Command {
    private final CollectionManager collection;

    public Show(CollectionManager collection) {
        this.collection = collection;
    }

    public void execute(String argument, Flat flat) {
        if (collection.getCollection().isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }

        System.out.println("Элементы коллекции:");
        for (Flat f : collection.getCollection()) {
            System.out.println(f.toString());
        }
    }

    public String getName() {
        return "show";
    }

    public String getDescription() {
        return "вывести все элементы коллекции в строковом представлении";
    }
}