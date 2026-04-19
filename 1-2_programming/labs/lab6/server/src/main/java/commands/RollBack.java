package commands;

import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда 'rollback'. Отменяет изменения, сделанные в рамках активной транзакции,
 * и возвращает коллекцию к исходному состоянию.
 *
 * @author Roman Golovin
 */
public class RollBack implements Command {
    private final CollectionManager collectionManager;

    public RollBack(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "rollback";
    }

    @Override
    public String getDescription() {
        return "откатывает изменения текущей транзакции";
    }

    @Override
    public String execute(String argument, Flat flat) {
        try {
            collectionManager.rollbackTransaction();
            return("Транзакция отменена. Коллекция возвращена в исходное состояние.");
        } catch (IllegalStateException e) {
            return("Ошибка: " + e.getMessage());
        }
    }
}