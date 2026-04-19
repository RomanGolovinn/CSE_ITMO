package commands;

import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда для фильтрации элементов коллекции по заданному дому.
 * Выводит в консоль все квартиры, значение поля house которых (а именно имя дома)
 * совпадает с переданным строковым аргументом.
 *
 * @author Roman Golovin
 */
public class FilterByHouse implements Command{
    private final CollectionManager collection;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, по которой будет производиться поиск
     */
    public FilterByHouse(CollectionManager collection){
        this.collection = collection;
    }

    /**
     * Выполняет команду фильтрации.
     * Ищет квартиры с совпадающим именем дома с помощью Stream API и выводит результат.
     *
     * @param argument название дома для поиска (не может быть null)
     * @param flat     объект квартиры (для данной команды не используется)
     */
    public String execute(String argument, Flat flat){
        if (argument == null){
            return ("Аргумент должен содержать имя дома, не может быть пустым");
        }
        System.out.println("Квартиры в доме " + argument +": ");
        Flat[] flatsInHouse = collection.getCollection().stream().filter(
                f -> f.getHouse() != null && f.getHouse().getName().equals(argument)
        ).toArray(Flat[]::new);

        if (flatsInHouse.length == 0){
            return("Не найдено квартир в доме");
        }
        String flatsToString = "";
        for (Flat f : flatsInHouse){
            //Конатенация строк в цикле это проблемы гелиуса и его оперативки
             flatsToString += f.toString();
        }
        return flatsToString;
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("filter_by_house")
     */
    public String getName(){
        return "filter_by_house";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription() {
        return "вывести элементы, значение поля house которых равно заданному";
    }
}