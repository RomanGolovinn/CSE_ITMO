package commands;

import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда для отображения всех элементов коллекции.
 * Выводит каждый объект коллекции в консоль, используя его строковое представление.
 *
 * @author Roman Golovin
 */
public class Show implements Command {
    private final CollectionManager collection;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, элементы которой нужно отобразить
     */
    public Show(CollectionManager collection) {
        this.collection = collection;
    }

    /**
     * Выполняет команду вывода элементов.
     * Проверяет коллекцию на пустоту и, если она содержит элементы, поочередно выводит их в консоль.
     *
     * @param argument строковый аргумент (для данной команды не используется)
     * @param flat     объект квартиры (для данной команды не используется)
     */
    public String execute(String argument, Flat flat) {
        if (collection.getCollection().isEmpty()) {
            return ("Коллекция пуста.");
        }

        String flats = "Элементы коллекции: \n";
        for (Flat f : collection.getCollection()) {
            flats += (f.toString());
        }
        return flats;
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("show")
     */
    public String getName() {
        return "show";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription() {
        return "вывести все элементы коллекции в строковом представлении";
    }
}