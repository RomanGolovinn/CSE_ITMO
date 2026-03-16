package io.file;


import managers.collection.CollectionManager;

abstract public class FileManager {
    String path;
    CollectionManager collection;

    public FileManager(String path, CollectionManager collection){
        this.path = path;
        this.collection = collection;
    }

    public abstract void save();
    public abstract void read();
}
