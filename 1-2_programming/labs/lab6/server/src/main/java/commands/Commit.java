package commands;

import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда 'commit'. Фиксирует изменения, сделанные в рамках активной транзакции.
 *
 * @author Roman Golovin
 */
public class Commit implements Command {
    private final CollectionManager collectionManager;

    public Commit(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "commit";
    }

    @Override
    public String getDescription() {
        return "фиксирует изменения текущей транзакции";
    }

    @Override
    public String execute(String argument, Flat flat) {
        try {
            collectionManager.commitTransaction();
            return ("Транзакция успешно зафиксирована. Изменения сохранены.");
        } catch (IllegalStateException e) {
            return("Ошибка: " + e.getMessage());
        }
    }
}