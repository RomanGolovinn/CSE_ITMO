package managers.collection;

import models.Flat;

import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

/**
 * Реализация менеджера коллекции, использующая {@link Stack} в качестве хранилища.
 * Обеспечивает выполнение всех основных операций над объектами типа {@link Flat}.
 * * @author Roman Golovin
 */
public class StackManager extends CollectionManager {
    /** Основное хранилище элементов коллекции */
    private Stack<Flat> flats = new Stack<>();

    private Stack<Flat> savedFlats = new Stack<>();

    /**
     * Добавляет новую квартиру в стек.
     *
     * @param flat объект квартиры для добавления
     */
    @Override
    public void add(Flat flat) {
        flats.push(flat);
    }

    /**
     * Удаляет квартиру из стека по её идентификатору.
     *
     * @param id уникальный идентификатор квартиры
     * @return true, если элемент был найден и удален, иначе false
     */
    @Override
    public boolean removeById(Long id) {
        return flats.removeIf(f -> f.getId().equals(id));
    }

    /**
     * Полностью очищает стек, удаляя все элементы.
     */
    @Override
    public void clear() {
        flats.clear();
    }

    /**
     * Ищет квартиру в стеке по её ID с использованием Stream API.
     *
     * @param id уникальный идентификатор для поиска
     * @return объект {@link Flat} или null, если ничего не найдено
     */
    @Override
    public Flat getById(Long id) {
        return flats.stream().filter(f -> f.getId().equals(id)).findFirst()
                .orElse(null);
    }

    /**
     * Возвращает текущий стек элементов.
     *
     * @return коллекция всех квартир в стеке
     */
    @Override
    public Collection<Flat> getCollection() {
        return flats;
    }

    /**
     * Сортирует стек в соответствии с естественным порядком элементов.
     */
    @Override
    public void sort() {
        Collections.sort(flats);
    }

    /**
     * Обновляет данные квартиры в стеке по её ID.
     * Перед заменой устанавливает новому объекту оригинальный ID старой квартиры.
     *
     * @param id      ID квартиры, которую нужно обновить
     * @param newFlat объект с новыми данными
     * @return true, если обновление прошло успешно, false если ID не найден
     */
    @Override
    public boolean update(Long id, Flat newFlat) {
        for (int i = 0; i < flats.size(); i++) {
            if (flats.get(i).getId().equals(id)) {

                // Важный момент: сохраняем старый ID для нового объекта
                newFlat.setId(id);

                flats.set(i, newFlat);
                return true;
            }
        }
        return false;
    }

    /**
     * Заменяет текущий стек новой коллекцией.
     * Приводит переданную коллекцию к типу {@link Stack}.
     *
     * @param newCollection новая коллекция элементов для загрузки
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setCollection(Collection<Flat> newCollection){
        this.flats = (Stack<Flat>) newCollection;
    }

    @Override
    public void beginTransaction() {
        if (this.isTransactionActive) {
            throw new IllegalStateException("Транзакция уже активна, сначала завершите текущую.");
        }
        this.isTransactionActive = true;
        this.savedFlats.clear();
        this.savedFlats.addAll(this.flats);
    }

    @Override
    public void commitTransaction() {
        if (!this.isTransactionActive) {
            throw new IllegalStateException("Нет активной транзакции");
        }
        this.isTransactionActive = false;
        this.savedFlats.clear();
    }

    @Override
    public void rollbackTransaction() {
        if (!this.isTransactionActive) {
            throw new IllegalStateException("Нет активной транзакции для отката");
        }
        this.isTransactionActive = false;

        this.flats.clear();
        this.flats.addAll(this.savedFlats);

        this.savedFlats.clear();
    }
}