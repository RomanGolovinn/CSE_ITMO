package commands;

import io.file.FileManager;
import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда для сохранения текущего состояния коллекции в файл.
 * Использует менеджер файлов для записи данных на диск.
 *
 * @author Roman Golovin
 */
public class Save implements Command{
    private final CollectionManager collection;
    private final FileManager file;

    /**
     * Конструктор команды.
     *
     * @param collection менеджер коллекции, состояние которой нужно сохранить
     * @param file       менеджер файлов, отвечающий за процесс записи
     */
    public Save(CollectionManager collection, FileManager file) {
        this.collection = collection;
        this.file = file;
    }

    /**
     * Выполняет команду сохранения.
     * Обращается к менеджеру файлов для записи текущих данных коллекции.
     *
     * @param argument строковый аргумент (для данной команды не используется)
     * @param flat     объект квартиры (для данной команды не используется)
     */
    public String execute(String argument, Flat flat) {
        file.save();
        return "";
    }

    /**
     * Возвращает имя команды.
     *
     * @return имя команды для вызова из консоли ("save")
     */
    public String getName() {
        return "save";
    }

    /**
     * Возвращает описание команды.
     *
     * @return краткое описание действия команды
     */
    public String getDescription() {
        return "сохранить коллекцию в файл";
    }

}