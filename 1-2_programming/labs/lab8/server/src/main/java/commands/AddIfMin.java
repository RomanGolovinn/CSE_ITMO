package commands;

import managers.collection.CollectionManager;
import models.Flat;

import java.util.Collections;

/**
 * Команда для добавления нового элемента в коллекцию, если его значение меньше,
 * чем у наименьшего элемента этой коллекции.
 * Сравнение элементов происходит на основе реализованного интерфейса Comparable.
 *
 * @author Roman Golovin
 */
public class AddIfMin implements Command{
    private final CollectionManager collection;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, с которым будет работать команда
     */
    public AddIfMin(CollectionManager collection){
        this.collection = collection;
    }

    /**
     * Выполняет команду проверки и добавления элемента.
     * Если коллекция пуста, элемент добавляется безусловно.
     * Иначе сравнивается с минимальным элементом коллекции.
     *
     * @param argument строковый аргумент команды (не используется)
     * @param flat     объект квартиры для потенциального добавления (не должен быть null)
     */
    public String execute(String argument, Flat flat){
        if (flat == null){
            return ("Не указана квартира");
        }

        if (collection.getCollection().isEmpty()) {
            collection.add(flat);
        } else {
            Flat minFlat = Collections.min(collection.getCollection());
            if (flat.compareTo(minFlat) < 0){
                collection.add(flat);
            }else{
                return ("Квартира больше или равна наименьшей квартире в коллекции");
            }
        }
        return ("Квартира добавлена в коллекцию");
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("add_if_min")
     */
    public String getName(){
        return "add_if_min";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription(){
        return "добавить новый элемент в коллекцию,"
                + "если его значение меньше, чем у наименьшего элемента этой коллекции";
    }
}