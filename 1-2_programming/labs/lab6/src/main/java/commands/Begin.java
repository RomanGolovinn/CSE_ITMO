package commands;

import managers.collection.CollectionManager;
import models.Flat;

/**
 * Команда 'begin'. Начинает новую транзакцию.
 * Сохраняет текущее состояние коллекции для возможности отката.
 *
 * @author Roman Golovin
 */
public class Begin implements Command {
    private final CollectionManager collectionManager;

    public Begin(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "begin";
    }

    @Override
    public String getDescription() {
        return "начинает новую транзакцию";
    }

    @Override
    public void execute(String argument, Flat flat) {
        try {
            collectionManager.beginTransaction();
            System.out.println("Транзакция успешно начата. Все последующие изменения можно будет отменить.");
        } catch (IllegalStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}