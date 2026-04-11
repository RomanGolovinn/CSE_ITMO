package io.file;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Адаптер типов для сериализации и десериализации объектов {@link LocalDateTime} в формат JSON.
 * Использует стандартный формат ISO-8601 (например, 2026-03-18T12:58:50).
 * * @author Roman Golovin
 */
public class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
    /** Форматтер для преобразования даты в строку и обратно */
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Записывает объект LocalDateTime в JSON.
     *
     * @param out   объект для записи JSON-данных
     * @param value объект даты и времени для записи (может быть null)
     * @throws IOException при ошибках записи в поток
     */
    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(formatter.format(value));
        }
    }

    /**
     * Считывает объект LocalDateTime из JSON.
     *
     * @param in объект для чтения JSON-данных
     * @return считанный объект LocalDateTime или null, если в JSON записано null
     * @throws IOException при ошибках чтения или парсинга строки
     */
    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return LocalDateTime.parse(in.nextString(), formatter);
    }
}