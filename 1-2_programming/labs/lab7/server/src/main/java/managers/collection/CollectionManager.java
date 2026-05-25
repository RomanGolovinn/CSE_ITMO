package managers.collection;

import models.Flat;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Абстрактный менеджер коллекции.
 * Содержит общую логику управления данными: отслеживание времени инициализации,
 * формирование информационной справки и описание контракта для работы с элементами {@link Flat}.
 *
 * @author Roman Golovin
 */
public abstract class CollectionManager {
    /** Время последней инициализации коллекции */
    protected LocalDateTime lastInitTime;
    /** Время последнего сохранения (опционально) */
    protected LocalDateTime lastSaveTime;

    protected boolean isTransactionActive = false;

    /**
     * Конструктор менеджера. Устанавливает время инициализации при создании объекта.
     */
    public CollectionManager() {
        this.lastInitTime = LocalDateTime.now();
    }

    /** Добавляет новую квартиру в коллекцию */
    public abstract void add(Flat flat);

    /** Удаляет квартиру из коллекции по её ID */
    public abstract boolean removeById(Long id);

    /** Полностью очищает коллекцию */
    public abstract void clear();

    /** Возвращает квартиру по её ID */
    public abstract Flat getById(Long id);

    /** Возвращает саму коллекцию объектов */
    public abstract Collection<Flat> getCollection();

    /** Сортирует коллекцию согласно заданному порядку */
    public abstract void sort();

    /** Обновляет данные квартиры с заданным ID */
    public abstract boolean update(Long id, Flat newFlat);

    /**
     * Формирует краткую информационную сводку о коллекции.
     *
     * @return строка с типом коллекции, временем инициализации и количеством элементов
     */
    public String getInfo() {
        return "Тип: " + getCollection().getClass().getSimpleName() +
                "\nИнициализирован: " + lastInitTime +
                "\nЭлементов: " + getCollection().size();
    }

    /**
     * Заменяет текущую коллекцию новой. Используется при загрузке данных из файла.
     *
     * @param newCollection новая коллекция элементов
     */
    public abstract void setCollection(Collection<Flat> newCollection);

    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public boolean isTransactionActive() {
        return isTransactionActive;
    }
}