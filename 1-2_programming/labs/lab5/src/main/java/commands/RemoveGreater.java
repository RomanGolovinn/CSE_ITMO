package commands;

import managers.collection.CollectionManager;
import models.Flat;

public class RemoveGreater implements Command {
    private final CollectionManager collection;

    public RemoveGreater(CollectionManager collection){
        this.collection = collection;
    }

    public void execute(String argument, Flat flat){
        if (flat == null){
            System.out.println("Не указана квартира");
            return;
        }

        Flat[] greaterFlats = collection.getCollection().stream().filter(
                f -> f.compareTo(flat)>0).toArray(Flat[]::new);

        for (Flat f : greaterFlats){
            Long id = f.getId();
            collection.removeById(id);
        }

        System.out.println("Все элементы превышающий заданный удалены");
    }

    public String getName(){
        return "remove_greater";
    }

    public String getDescription(){
        return "удалить из коллекции все элементы, превышающие заданный";
    }
}
