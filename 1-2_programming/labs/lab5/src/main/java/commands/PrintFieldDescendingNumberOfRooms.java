package commands;

import main.java.managers.collection.CollectionManager;
import main.java.managers.models.Flat;

import java.util.Comparator;
import java.util.Objects;

public class PrintFieldDescendingNumberOfRooms implements Command{
    private final CollectionManager collection;

    public PrintFieldDescendingNumberOfRooms(CollectionManager collection){
        this.collection = collection;
    }

    public void execute(String argument, Flat flatArgument){
        Long[] descNumberOfRooms = collection.getCollection().stream()
                .map(Flat::getNumberOfRooms)
                .filter(Objects::nonNull).sorted(Comparator.reverseOrder())
                .toArray(Long[]::new);

        if (descNumberOfRooms.length == 0){
            System.out.println("В коллекции нет квартир с комнатами");
            return;
        }

        System.out.println("Количество комнат в порядке убывания");
        for (long n : descNumberOfRooms){
            System.out.println(n);
        }
    }

    public String getName(){
        return "print_field_descending_number_of_rooms";
    }

    public String getDescription(){
        return "вывести значения поля numberOfRooms всех элементов в порядке убывания";
    }
}
