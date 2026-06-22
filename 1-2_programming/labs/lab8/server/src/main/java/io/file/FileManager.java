package io.file;

import managers.collection.CollectionManager;

/**
 * Абстрактный класс для управления операциями ввода-вывода коллекции.
 * Служит базой для реализации конкретных парсеров (например, JSON или XML).
 * * @author Roman Golovin
 */
abstract public class FileManager {
    /** Путь к файлу данных */
    protected String path;
    /** Менеджер коллекции, с которым синхронизируются данные */
    protected CollectionManager collection;

    /**
     * Конструктор файлового менеджера.
     *
     * @param path       путь к файлу для чтения и записи
     * @param collection менеджер коллекции для манипуляции данными
     */
    public FileManager(String path, CollectionManager collection){
        this.path = path;
        this.collection = collection;
    }

    /**
     * Абстрактный метод для сохранения текущего состояния коллекции в файл.
     */
    public abstract void save();

    /**
     * Абстрактный метод для чтения данных из файла и наполнения коллекции.
     */
    public abstract void read();
}