package models;

/**
 * Класс, представляющий сущность Дома.
 * Содержит информацию о здании, в котором располагается квартира.
 *
 * @author Roman Golovin
 */
public class House {
    /**
     * Название дома.
     */
    private String name; //Поле может быть null

    /**
     * Год постройки дома.
     */
    private Integer year; //Поле не может быть null, Значение поля должно быть больше 0

    /**
     * Количество этажей в доме.
     */
    private long numberOfFloors; //Значение поля должно быть больше 0

    /**
     * Конструктор для создания нового объекта дома с заданными параметрами.
     *
     * @param name           название дома (может быть null)
     * @param year           год постройки (не может быть null, должен быть больше 0)
     * @param numberOfFloors количество этажей (должно быть больше 0)
     */
    public House(String name, Integer year, long numberOfFloors) {
        this.name = name;
        this.year = year;
        this.numberOfFloors = numberOfFloors;
    }

    /**
     * Возвращает год постройки дома.
     *
     * @return год постройки
     */
    public Integer getYear(){
        return year;
    }

    /**
     * Возвращает название дома.
     *
     * @return название дома
     */
    public String getName(){
        return name;
    }

    /**
     * Возвращает строковое представление объекта дома.
     *
     * @return отформатированная строка с данными о доме
     */
    @Override
    public String toString() {
        return "{name='" + name + "', year=" + year + ", floors=" + numberOfFloors + "}";
    }
}