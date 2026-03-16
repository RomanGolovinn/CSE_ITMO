package commands;

import main.java.managers.collection.CollectionManager;
import main.java.managers.models.Flat;

public class Sort implements Command {
    private final CollectionManager collection;

    public Sort(CollectionManager collection){
        this.collection = collection;
    }

    public void execute(String argument, Flat flatArgument){
        collection.sort();
        System.out.println("Коллекция отсортирована");
    }

    public String getName(){
        return "sort";
    }

    public String getDescription(){
       return "отсортировать коллекцию в естественном порядке";
    }
}
