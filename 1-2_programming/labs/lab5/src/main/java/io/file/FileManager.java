package io.file;


import main.java.managers.collection.CollectionManager;

abstract public class FileManager {
    String path;
    CollectionManager collection;

    public FileManager(String path, CollectionManager collection){
        this.path = path;
        this.collection = collection;
    }

    abstract void save();
    abstract void read();
}
