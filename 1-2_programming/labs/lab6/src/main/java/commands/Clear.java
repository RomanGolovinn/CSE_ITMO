package commands;

import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда для очистки коллекции.
 * Удаляет все элементы, находящиеся в текущей коллекции.
 *
 * @author Roman Golovin
 */
public class Clear implements Command {
    private final CollectionManager collection;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, который будет очищен
     */
    public Clear(CollectionManager collection) {
        this.collection = collection;
    }

    /**
     * Выполняет команду очистки.
     * Вызывает соответствующий метод у менеджера коллекции и выводит сообщение об успехе.
     *
     * @param argument строковый аргумент команды (для данной команды не используется)
     * @param flat     объект квартиры (для данной команды не используется)
     */
    public void execute(String argument, Flat flat) {
        collection.clear();
        System.out.println("Коллекция успешно очищена.");
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("clear")
     */
    public String getName() {
        return "clear";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription() {
        return "очистить коллекцию";
    }
}