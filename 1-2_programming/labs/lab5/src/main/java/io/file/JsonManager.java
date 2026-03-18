package io.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import managers.collection.CollectionManager;
import models.Flat;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Stack;

public class JsonManager extends FileManager{
    Gson jsonHandler;

    public JsonManager(String path, CollectionManager collection){
        super(path, collection);
        this.jsonHandler = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
    }

    @Override
    public void save() {
        try {
            FileOutputStream fos = new FileOutputStream(path);

            String jsonString = jsonHandler.toJson(collection.getCollection());

            fos.write(jsonString.getBytes());

            fos.close();

            System.out.println("Коллекция сохранена");

        } catch (Exception e) {
            System.out.println("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    @Override
    public void read() {
        Stack<Flat> localCollection = new Stack<>();

        try (FileReader reader = new FileReader(path)) {
            Type collectionType = new TypeToken<Stack<Flat>>(){}.getType();
            Stack<Flat> loadedCollection = jsonHandler.fromJson(reader, collectionType);

            if (loadedCollection != null) {
                localCollection = loadedCollection;
            }

        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.out.println("Ошибка синтаксиса в файле JSON: " + e.getMessage());
        }

        localCollection.removeIf(flat -> !flat.isValid());

        this.collection.setCollection(localCollection);
    }
}
