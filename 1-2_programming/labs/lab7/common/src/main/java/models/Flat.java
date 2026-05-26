package models;

import models.enums.Furnish;
import models.enums.Transport;
import models.enums.View;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс, представляющий сущность Квартиры.
 * Объекты этого класса хранятся в коллекции и управляются пользователем.
 *
 * @author Roman Golovin
 */
public class Flat implements Comparable<Flat>, Serializable {
    /**
     * Уникальный идентификатор квартиры.
     * Значение поля должно быть больше 0. Генерируется автоматически.
     */
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    /**
     * Название квартиры.
     * Поле не может быть null, строка не может быть пустой.
     */
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**
     * Координаты квартиры.
     * Поле не может быть null.
     */
    private Coordinates coordinates; //Поле не может быть null
    /**
     * Дата создания записи о квартире.
     * Поле не может быть null, генерируется автоматически при создании объекта.
     */
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    /**
     * Площадь квартиры.
     * Значение поля должно быть больше 0.
     */
    private Long area; //Поле может быть null, Значение поля должно быть больше 0
    /**
     * Количество комнат в квартире.
     * Значение поля должно быть больше 0.
     */
    private Long numberOfRooms; //Поле может быть null, Значение поля должно быть больше 0

    /**
     * Отделка квартиры.
     * Поле не может быть null.
     */
    private Furnish furnish; //Поле не может быть null

    /**
     * Вид из квартиры.
     * Поле не может быть null.
     */
    private View view; //Поле не может быть null

    /**
     * Доступный транспорт.
     * Поле может быть null.
     */
    private Transport transport; //Поле может быть null
    /**
     * Дом, в котором находится квартира.
     * Поле может быть null.
     */
    private House house; //Поле может быть null

    private Integer ownerId;

    /**
     * Конструктор для создания нового объекта квартиры.
     * Идентификатор (id) не задается в конструкторе, а дата создания генерируется автоматически.
     *
     * @param name          название квартиры
     * @param coordinates   координаты квартиры
     * @param area          площадь квартиры
     * @param numberOfRooms количество комнат
     * @param furnish       отделка квартиры
     * @param view          вид из квартиры
     * @param transport     доступный транспорт
     * @param house         дом, в котором находится квартира
     */
    public Flat(String name, Coordinates coordinates, Long area, Long numberOfRooms,
                Furnish furnish, View view, Transport transport, House house) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.furnish = furnish;
        this.view = view;
        this.transport = transport;
        this.house = house;
    }

    /**
     * Устанавливает уникальный идентификатор квартиры.
     *
     * @param id новый идентификатор
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * Возвращает уникальный идентификатор квартиры.
     *
     * @return идентификатор квартиры
     */
    public Long getId(){return id;}

    /**
     * Возвращает название квартиры.
     *
     * @return название квартиры
     */
    public String getName() { return name; }

    /**
     * Возвращает площадь квартиры.
     *
     * @return площадь квартиры
     */
    public Long getArea() { return area; }

    /**
     * Возвращает количество комнат в квартире.
     *
     * @return количество комнат
     */
    public Long getNumberOfRooms() { return numberOfRooms; }

    /**
     * Возвращает дом, в котором находится квартира.
     *
     * @return объект дома
     */
    public House getHouse() { return house; }

    /**
     * Проверяет валидность всех полей квартиры согласно заданным ограничениям.
     *
     * @return true, если все поля валидны, иначе false
     */
    public boolean isValid() {
        if (id == null || id <= 0) return false;
        if (name == null || name.trim().isEmpty()) return false;
        if (coordinates == null || coordinates.getX() > 853) return false;
        if (area == null || area <= 0) return false;
        if (numberOfRooms == null || numberOfRooms <= 0) return false;
        if (creationDate == null) return false;
        if (house != null && house.getYear() <= 0) return false;

        return true;
    }

    /**
     * Сравнивает данную квартиру с другой по названию.
     *
     * @param other объект квартиры для сравнения
     * @return отрицательное целое число, ноль или положительное целое число, если это название меньше, равно или больше указанного
     */
    @Override
    public int compareTo(Flat other) {
        return this.name.compareTo(other.getName());
    }

    /**
     * Проверяет равенство данной квартиры с другим объектом.
     * Сравнение происходит по полям id, name и coordinates.
     *
     * @param o объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flat flat = (Flat) o;
        return Objects.equals(id, flat.id) &&
                Objects.equals(name, flat.name) &&
                Objects.equals(coordinates, flat.coordinates);
    }

    /**
     * Возвращает хэш-код квартиры, вычисленный на основе полей id, name и coordinates.
     *
     * @return значение хэш-кода
     */
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
    }

    /**
     * Возвращает строковое представление квартиры со значениями всех полей.
     *
     * @return отформатированная строка с данными
     */
    @Override
    public String toString() {
        return String.format("{ID:%d, Name:%s, Coordinates:%s, Area:%d, Rooms:%d, Furnish:%s,"
                        +" View:%s, Transport:%s, House:%s}",
                id, name, coordinates.toString(), area, numberOfRooms, furnish, view, transport,
                house.toString());
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Furnish getFurnish() {
        return furnish;
    }

    public View getView() {
        return view;
    }

    public Transport getTransport() {
        return transport;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}