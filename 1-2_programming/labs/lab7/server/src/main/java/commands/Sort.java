package commands;

import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда для сортировки коллекции в естественном порядке.
 * Сортировка выполняется на основе реализации интерфейса Comparable в классе Flat.
 *
 * @author Roman Golovin
 */
public class Sort implements Command {
    private final CollectionManager collection;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, которую необходимо отсортировать
     */
    public Sort(CollectionManager collection){
        this.collection = collection;
    }

    /**
     * Выполняет команду сортировки.
     * Вызывает метод сортировки у менеджера коллекции и выводит подтверждение пользователю.
     *
     * @param argument     строковый аргумент (для данной команды не используется)
     * @param flatArgument объект квартиры (для данной команды не используется)
     */
    public String execute(String argument, Flat flatArgument){
        collection.sort();
        return ("Коллекция отсортирована");
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("sort")
     */
    public String getName(){
        return "sort";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription(){
        return "отсортировать коллекцию в естественном порядке";
    }
}