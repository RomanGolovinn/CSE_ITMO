package commands;

import managers.collection.CollectionManager;
import models.Flat;

import java.util.Collections;

public class AddIfMin implements Command{
    private final CollectionManager collection;

    public AddIfMin(CollectionManager collection){
        this.collection = collection;
    }

    public void execute(String argument, Flat flat){
        if (flat == null){
            System.out.println("Не указана квартира");
            return;
        }

        if (collection.getCollection().isEmpty()) {
            collection.add(flat);
        } else {
            Flat minFlat = Collections.min(collection.getCollection());
            if (flat.compareTo(minFlat) < 0){
                collection.add(flat);
            }else{
                System.out.println("Квартира больше или равна наименьшей квартире в коллекции");
                return;
            }
        }
        System.out.println("Квартира добавлена в коллекцию");
    }

    public String getName(){
        return "add_if_min";
    }

    public String getDescription(){
        return "добавить новый элемент в коллекцию,"
        + "если его значение меньше, чем у наименьшего элемента этой коллекции";
    }
}
