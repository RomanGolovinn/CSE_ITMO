package commands;

import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда для добавления нового элемента (квартиры) в коллекцию.
 * Автоматически генерирует уникальный идентификатор для нового элемента перед его сохранением.
 *
 * @author Roman Golovin
 */
public class Add implements Command {
    private final CollectionManager collection;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, в которую будет добавлен элемент
     */
    public Add(CollectionManager collection){
        this.collection = collection;
    }

    /**
     * Выполняет команду добавления элемента в коллекцию.
     * Автоматически вычисляет максимальный существующий ID и назначает новому элементу ID на единицу больше.
     *
     * @param argument строковый аргумент команды (для данной команды не используется)
     * @param flat     объект квартиры для добавления (не должен быть null)
     */
    public String execute(String argument, Flat flat){
        if (flat == null){
            return "Ошибка: Flat не может быть null";
        }

        long id = 1L;
        if (!collection.getCollection().isEmpty()){
            id = collection.getCollection().stream()
                    .map(Flat::getId)
                    .max(Long::compareTo)
                    .get()+1L;
        }

        try{
            flat.setId(id);
            collection.add(flat);
            return "Квартира успешна добавлена";
        }catch (Exception e){
            return "Ошибка: " + e.getMessage();
        }
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("add")
     */
    public String getName(){
        return "add";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription(){
        return "добавить новый элемент в коллекцию";
    }
}