package commands;

import managers.collection.CollectionManager;
import models.Flat;

public class RemoveById implements Command {
    private final CollectionManager collection;

    public RemoveById(CollectionManager collection) {
        this.collection = collection;
    }

    @Override
    public void execute(String argument, Flat flat) {
        if (argument == null || argument.isEmpty()) {
            System.out.println("Ошибка: необходимо указать ID для удаления.");
            return;
        }

        try {
            Long id = Long.parseLong(argument.trim());
            boolean success = collection.removeById(id);

            if (success) {
                System.out.println("Квартира с ID " + id + " успешно удалена.");
            } else {
                System.out.println("Квартира с ID " + id + " не найдена.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом.");
        }
    }

    @Override
    public String getName() { return "remove_by_id"; }

    @Override
    public String getDescription() { return "удалить элемент из коллекции по его id"; }
}