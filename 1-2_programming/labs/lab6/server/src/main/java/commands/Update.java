package commands;

import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда для обновления данных существующего элемента коллекции.
 * Находит квартиру по указанному ID и заменяет её поля новыми данными.
 * Требует как строковый аргумент (ID), так и готовый объект Flat.
 *
 * @author Roman Golovin
 */
public class Update implements Command {
    private final CollectionManager collection;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, в которой будет произведено обновление
     */
    public Update(CollectionManager collection) {
        this.collection = collection;
    }

    /**
     * Выполняет команду обновления квартиры.
     * Проверяет наличие ID, валидность формата числа и существование объекта в коллекции
     * перед выполнением операции обновления.
     *
     * @param argument строковое представление ID квартиры, которую нужно обновить
     * @param flat     новый объект квартиры с актуальными данными
     */
    public String execute(String argument, Flat flat) {
        if (argument == null || argument.isEmpty()) {
            return ("Ошибка: необходимо указать ID квартиры для обновления.");
        }
        if (flat == null) {
            return ("Ошибка: объект Flat не передан.");
        }

        try {
            Long id = Long.parseLong(argument.trim());
            Flat oldFlat = collection.getById(id);

            if (oldFlat == null) {
                return ("Квартира с ID " + id + " не найдена.");
            }

            boolean success = collection.update(id, flat);

            if (success){
                return ("Квартира успешно обновлена!");
            }else {
                return ("Квартира с ID " + id + " не найдена.");
            }

        } catch (NumberFormatException e) {
            return ("Ошибка: ID должен быть числом.");
        }
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("update")
     */
    public String getName() {
        return "update";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}