package main.java.managers.models;

public class Coordinates {
    private Long x; //Максимальное значение поля: 853, Поле не может быть null
    private float y; //Значение поля должно быть больше -226
    public Coordinates(Long x, float y) {
        this.x = x;
        this.y = y;
    }

    public Long getX() { return x; }
    public float getY() { return y; }

    @Override
    public String toString() {
        return "X:" + x + ", Y:" + y;
    }
}
