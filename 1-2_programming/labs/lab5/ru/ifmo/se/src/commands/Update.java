package commands;

import managers.collection.CollectionManager;
import models.Flat;

public class Update implements Command {
    private final CollectionManager collection;

    public Update(CollectionManager collection) {
        this.collection = collection;
    }

    public void execute(String argument, Flat flat) {
        if (argument == null || argument.isEmpty()) {
            System.out.println("Ошибка: необходимо указать ID квартиры для обновления.");
            return;
        }
        if (flat == null) {
            System.out.println("Ошибка: объект Flat не передан.");
            return;
        }

        try {
            Long id = Long.parseLong(argument.trim());
            Flat oldFlat = collection.getById(id);

            if (oldFlat == null) {
                System.out.println("Квартира с ID " + id + " не найдена.");
                return;
            }

            boolean success = collection.update(id, flat);

            if (success){
                System.out.println("Квартира успешно обновлена!");
            }else {
                System.out.println("Квартира с ID " + id + " не найдена.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом.");
        }
    }

    public String getName() {
        return "update";
    }

    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}