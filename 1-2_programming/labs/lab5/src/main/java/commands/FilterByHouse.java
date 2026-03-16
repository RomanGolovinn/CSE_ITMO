package commands;

import main.java.managers.collection.CollectionManager;
import main.java.managers.models.Flat;

public class FilterByHouse implements Command{
    private final CollectionManager collection;

    public FilterByHouse(CollectionManager collection){
        this.collection = collection;
    }

    public void execute(String argument, Flat flat){
        if (argument == null){
            System.out.println("Аргумент должен содержать имя дома, не может быть пустым");
            return;
        }
        System.out.println("Квартиры в доме " + argument +": ");
        Flat[] flatsInHouse = collection.getCollection().stream().filter(
                f -> f.getHouse() != null && f.getHouse().getName().equals(argument)
        ).toArray(Flat[]::new);

        if (flatsInHouse.length == 0){
            System.out.println("Не найдено квартир в доме");
            return;
        }
        for (Flat f : flatsInHouse){
            System.out.println(f);
        }
    }

    public String getName(){
        return "filter_by_house";
    }

    public String getDescription() {
        return "вывести элементы, значение поля house которых равно заданному";
    }
}
