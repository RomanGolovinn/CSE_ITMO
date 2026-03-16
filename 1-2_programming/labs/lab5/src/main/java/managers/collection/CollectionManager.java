package main.java.managers.collection;

import main.java.managers.models.Flat;

import java.time.LocalDateTime;
import java.util.Collection;

public abstract class CollectionManager {
    protected LocalDateTime lastInitTime;
    protected LocalDateTime lastSaveTime;

    public CollectionManager() {
        this.lastInitTime = LocalDateTime.now();
    }

    public abstract void add(Flat flat);
    public abstract boolean removeById(Long id);
    public abstract void clear();
    public abstract Flat getById(Long id);
    public abstract Collection<Flat> getCollection();
    public abstract void sort();
    public abstract boolean update(Long id, Flat newFlat);

    public String getInfo() {
        return "Тип: " + getCollection().getClass().getSimpleName() +
                "\nИнициализирован: " + lastInitTime +
                "\nЭлементов: " + getCollection().size();
    }
}