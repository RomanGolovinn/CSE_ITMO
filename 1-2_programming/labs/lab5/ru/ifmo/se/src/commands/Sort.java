package commands;

import managers.collection.CollectionManager;
import models.Flat;

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
