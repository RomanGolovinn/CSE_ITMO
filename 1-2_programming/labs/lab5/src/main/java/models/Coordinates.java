package models;

/**
 * Класс, представляющий координаты местоположения квартиры.
 * Содержит координаты по осям X и Y.
 *
 * @author Roman Golovin
 */
public class Coordinates {
    /**
     * Координата X.
     */
    private Long x; //Максимальное значение поля: 853, Поле не может быть null

    /**
     * Координата Y.
     */
    private float y; //Значение поля должно быть больше -226

    /**
     * Конструктор для создания нового объекта координат.
     *
     * @param x координата X (максимум 853, не может быть null)
     * @param y координата Y (должна быть больше -226)
     */
    public Coordinates(Long x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает значение координаты X.
     *
     * @return координата X
     */
    public Long getX() { return x; }

    /**
     * Возвращает значение координаты Y.
     *
     * @return координата Y
     */
    public float getY() { return y; }

    /**
     * Возвращает строковое представление координат для вывода в консоль.
     *
     * @return отформатированная строка с координатами X и Y
     */
    @Override
    public String toString() {
        return "{X:" + x + ", Y:" + y + "}";
    }
}