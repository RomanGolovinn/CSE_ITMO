package commands;

import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда для удаления из коллекции всех элементов, превышающих заданный.
 * Сравнение элементов происходит на основе реализованного интерфейса Comparable.
 *
 * @author Roman Golovin
 */
public class RemoveGreater implements Command {
    private final CollectionManager collection;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, из которой будут удалены элементы
     */
    public RemoveGreater(CollectionManager collection){
        this.collection = collection;
    }

    /**
     * Выполняет команду удаления элементов, превышающих заданный.
     * Находит все большие элементы с помощью Stream API и затем последовательно удаляет их по ID.
     *
     * @param argument строковый аргумент (для данной команды не используется)
     * @param flat     объект квартиры, с которым будут сравниваться элементы коллекции (не должен быть null)
     */
    public String execute(String argument, Flat flat){
        if (flat == null){
            return ("Не указана квартира");

        }

        Flat[] greaterFlats = collection.getCollection().stream().filter(
                f -> f.compareTo(flat)>0).toArray(Flat[]::new);

        for (Flat f : greaterFlats){
            Long id = f.getId();
            collection.removeById(id);
        }

        return ("Все элементы превышающий заданный удалены");
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("remove_greater")
     */
    public String getName(){
        return "remove_greater";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription(){
        return "удалить из коллекции все элементы, превышающие заданный";
    }
}