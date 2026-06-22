package commands;

import io.auth.UserContext;
import managers.collection.CollectionManager;
import models.Flat;

public class Add implements Command {
    private final CollectionManager collection;

    public Add(CollectionManager collection) {
        this.collection = collection;
    }

    @Override
    public String execute(String argument, Flat flat) {
        if (flat == null) {
            return "Ошибка: Flat не может быть null";
        }

        int ownerId = UserContext.getId();
        if (ownerId == -1) {
            return "Ошибка авторизации: пользователь не найден";
        }

        return collection.addFlat(flat, ownerId);
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }
}