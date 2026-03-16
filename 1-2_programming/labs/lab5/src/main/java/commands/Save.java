package commands;

import io.file.FileManager;
import managers.collection.CollectionManager;
import models.Flat;

public class Save implements Command{
    private final CollectionManager collection;
    private final FileManager file;

    public Save(CollectionManager collection, FileManager file) {
        this.collection = collection;
        this.file = file;
    }

    public void execute(String argument, Flat flat) {
        file.save();
        System.out.println("Коллекция сохранена в файл");
    }

    public String getName() {
        return "save";
    }

    public String getDescription() {
        return "сохранить коллекцию в файл";
    }

}
