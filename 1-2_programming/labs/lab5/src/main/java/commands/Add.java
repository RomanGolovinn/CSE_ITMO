package commands;

import managers.collection.CollectionManager;
import models.Flat;

public class Add implements Command {
    private final CollectionManager collection;

    public Add(CollectionManager collection){
        this.collection = collection;
    }

    public void execute(String argument, Flat flat){
        if (flat == null){
            System.out.println("Ошибка: Flat не может быть null");
            return;
        }

        long id = 1L;
        if (!collection.getCollection().isEmpty()){
            id = collection.getCollection().stream()
                    .map(Flat::getId)
                    .max(Long::compareTo)
                    .get()+1L;
        }

        try{
            flat.setId(id);
            collection.add(flat);
            System.out.println("Квартира успешна добавлена");
        }catch (Exception e){
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public String getName(){
        return "add";
    }

    public String getDescription(){
        return "добавить новый элемент в коллекцию";
    }
}
