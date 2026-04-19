package commands;

import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда для удаления элемента из коллекции по его уникальному идентификатору (ID).
 * Осуществляет проверку введенного аргумента на валидность и обрабатывает ошибки формата числа.
 *
 * @author Roman Golovin
 */
public class RemoveById implements Command {
    private final CollectionManager collection;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, из которой будет удален элемент
     */
    public RemoveById(CollectionManager collection) {
        this.collection = collection;
    }

    /**
     * Выполняет команду удаления квартиры по заданному ID.
     * Проверяет наличие аргумента, пытается преобразовать его в число (Long)
     * и вызывает соответствующий метод у менеджера коллекции.
     *
     * @param argument строковое представление ID квартиры, которую нужно удалить
     * @param flat     объект квартиры (для данной команды не используется)
     */
    @Override
    public String execute(String argument, Flat flat) {
        if (argument == null || argument.isEmpty()) {
            return ("Ошибка: необходимо указать ID для удаления.");
        }

        try {
            Long id = Long.parseLong(argument.trim());
            boolean success = collection.removeById(id);

            if (success) {
                return ("Квартира с ID " + id + " успешно удалена.");
            } else {
                return ("Квартира с ID " + id + " не найдена.");
            }
        } catch (NumberFormatException e) {
            return ("Ошибка: ID должен быть числом.");
        }
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("remove_by_id")
     */
    @Override
    public String getName() { return "remove_by_id"; }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    @Override
    public String getDescription() { return "удалить элемент из коллекции по его id"; }
}