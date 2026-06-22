package managers.collection;

import io.db.FlatDatabaseManager;
import models.Flat;

import java.time.LocalDateTime;
import java.util.Collection;

public abstract class CollectionManager {
    protected LocalDateTime lastInitTime;
    protected LocalDateTime lastSaveTime;
    protected boolean isTransactionActive = false;
    protected FlatDatabaseManager dbManager;

    public CollectionManager() {
        this.lastInitTime = LocalDateTime.now();
    }

    public void setDbManager(FlatDatabaseManager dbManager) {
        this.dbManager = dbManager;
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

    public abstract void setCollection(Collection<Flat> newCollection);

    public abstract void beginTransaction();

    public abstract void commitTransaction();

    public abstract void rollbackTransaction();

    public boolean isTransactionActive() {
        return isTransactionActive;
    }

    public String addFlat(Flat flat, int ownerId) {
        if (dbManager != null) {
            Long generatedId = dbManager.addFlat(flat, ownerId);
            if (generatedId != -1L) {
                flat.setId(generatedId);
                flat.setOwnerId(ownerId);
                this.add(flat);
                return "Квартира успешно добавлена. ID: " + generatedId;
            } else {
                return "Ошибка: не удалось сохранить квартиру в базу данных.";
            }
        }
        return "Ошибка: менеджер базы данных не подключен.";
    }

    public String removeFlat(Long id, int ownerId) {
        if (dbManager != null) {
            boolean dbRemoved = dbManager.removeById(id, ownerId);
            if (dbRemoved) {
                this.removeById(id);
                return "Квартира успешно удалена.";
            } else {
                return "Ошибка: квартира не найдена или у вас нет прав на удаление.";
            }
        }
        return "Ошибка: менеджер базы данных не подключен.";
    }

    public void loadFromDatabase() {
        if (dbManager != null) {
            this.setCollection(dbManager.loadCollection());
        }
    }
}