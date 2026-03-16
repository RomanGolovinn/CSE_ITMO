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

        } catch (Exception e) {
            System.out.println("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    @Override
    public void read() {
        try {
            File file = new File(path);

            if (!file.exists()) {
                System.out.println("Файл не найден. Будет создана новая коллекция.");
                return;
            }

            Scanner scanner = new Scanner(file);
            String jsonString = "";

            while (scanner.hasNextLine()) {
                jsonString += scanner.nextLine();
                // Хана оперативке
            }
            scanner.close();

            if (jsonString.isEmpty()) {
                return;
            }

            Type type = new TypeToken<Stack<Flat>>(){}.getType();
            Stack<Flat> loaded = jsonHandler.fromJson(jsonString, type);

            if (loaded != null) {
                Stack<Flat> current = (Stack<Flat>) collection.getCollection();
                current.clear();
                current.addAll(loaded);
            }

        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}
