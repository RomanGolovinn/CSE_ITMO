package commands;

import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда для вывода информации о коллекции.
 * Отображает основные сведения, такие как тип коллекции, дата инициализации и количество элементов.
 *
 * @author Roman Golovin
 */
public class Info implements Command {
    private final CollectionManager collection;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, информацию о которой необходимо вывести
     */
    public Info(CollectionManager collection) {
        this.collection = collection;
    }

    /**
     * Выполняет команду вывода информации.
     * Запрашивает данные у менеджера коллекции и выводит их в консоль.
     *
     * @param argument строковый аргумент (для данной команды не используется)
     * @param flat     объект квартиры (для данной команды не используется)
     */
    public String execute(String argument, Flat flat) {
        return("Информация о коллекции:\n") + collection.getInfo();
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("info")
     */
    public String getName() {
        return "info";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription() {
        return "вывести в стандартный поток вывода информацию о коллекции";
    }
}