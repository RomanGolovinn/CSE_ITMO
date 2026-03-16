package io.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.managers.collection.CollectionManager;

public class JsonManager extends FileManager{
    Gson jsonHandler;

    public JsonManager(String path, CollectionManager collection){
        super(path, collection);
        this.jsonHandler = new Gson();
    }

    @Override
    public void save(){

        for (var f : collection.getCollection()){

        }
    }

    @Override
    public void read(){

    }
}
