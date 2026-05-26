package commands;

import io.auth.UserContext;
import managers.collection.CollectionManager;
import models.Flat;

public class RemoveById implements Command {
    private final CollectionManager collection;

    public RemoveById(CollectionManager collection) {
        this.collection = collection;
    }

    @Override
    public String execute(String argument, Flat flat) {
        if (argument == null || argument.isEmpty()) {
            return "Ошибка: необходимо указать ID для удаления";
        }

        try {
            Long id = Long.parseLong(argument);
            int ownerId = UserContext.getId();

            if (ownerId == -1) {
                return "Ошибка авторизации: пользователь не найден";
            }

            return collection.removeFlat(id, ownerId);

        } catch (NumberFormatException e) {
            return "Ошибка: ID должен быть числом";
        }
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }
}