package commands;

import managers.collection.CollectionManager;
import models.Flat;

import java.util.Comparator;

/**
 * Команда для вывода элементов коллекции в порядке убывания.
 * Сортировка производится на основе естественного порядка элементов (интерфейса Comparable),
 * но в обратном направлении.
 *
 * @author Roman Golovin
 */
public class PrintDescending implements Command{
    private final CollectionManager collection;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, элементы которой будут отсортированы и выведены
     */
    public PrintDescending(CollectionManager collection){
        this.collection = collection;
    }

    /**
     * Выполняет команду сортировки и вывода элементов.
     * Использует Stream API для создания массива отсортированных по убыванию элементов коллекции,
     * а затем поочередно выводит их в консоль.
     *
     * @param argument     строковый аргумент (для данной команды не используется)
     * @param flatArgument объект квартиры (для данной команды не используется)
     */
    public String execute(String argument, Flat flatArgument){
        Flat[] descFlats = collection.getCollection().stream().sorted(
                Comparator.reverseOrder()).toArray(Flat[]::new);

        if (descFlats.length == 0){
            return ("Коллекция пуста");
        }
        String flats = "";
        for (Flat f : descFlats){
            flats += f;
        }
        return flats;
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("print_descending")
     */
    public String getName(){
        return "print_descending";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription(){
        return "вывести элементы коллекции в порядке убывания";
    }
}