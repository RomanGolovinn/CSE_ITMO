package commands;

import main.java.managers.collection.CollectionManager;
import main.java.managers.models.Flat;

import java.util.Comparator;

public class PrintDescending implements Command{
    private final CollectionManager collection;

    public PrintDescending(CollectionManager collection){
        this.collection = collection;
    }

    public void execute(String argument, Flat flatArgument){
        Flat[] descFlats = collection.getCollection().stream().sorted(
                Comparator.reverseOrder()).toArray(Flat[]::new);

        if (descFlats.length == 0){
            System.out.println("Коллекция пуста");
            return;
        }
        for (Flat f : descFlats){
            System.out.println(f);
        }
    }

    public String getName(){
        return "print_descending";
    }

    public String getDescription(){
        return "вывести элементы коллекции в порядке убывания";
    }
}
