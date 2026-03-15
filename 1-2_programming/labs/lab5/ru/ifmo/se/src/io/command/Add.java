package io.command;

import managers.collection.CollectionManager;
import models.Flat;

public class Add implements Command {
    CollectionManager Collection;
    public Add(CollectionManager collection){
        this.Collection = collection;
    }
    public void execute(String argument, Flat flat){
        if (flat == null){
            System.out.println("Ошибка: Flat не может быть null");
        }
        try{
            Collection.add(flat);
            System.out.println("Квартира успешна добавлена");
        }catch (Exception e){
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public String getName(){
        return "add";
    }

    public String getDescription(){
        return "add {element}: добавить новый элемент в коллекцию";
    }
}
