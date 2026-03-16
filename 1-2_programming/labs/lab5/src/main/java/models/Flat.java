package main.java.managers.models;

import main.java.managers.models.enums.Furnish;
import main.java.managers.models.enums.Transport;
import main.java.managers.models.enums.View;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flat implements Comparable<Flat>{
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long area; //Поле может быть null, Значение поля должно быть больше 0
    private Long numberOfRooms; //Поле может быть null, Значение поля должно быть больше 0
    private Furnish furnish; //Поле не может быть null
    private View view; //Поле не может быть null
    private Transport transport; //Поле может быть null
    private House house; //Поле может быть null

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

    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){return id;}
    public String getName() { return name; }
    public Long getArea() { return area; }
    public Long getNumberOfRooms() { return numberOfRooms; }
    public House getHouse() { return house; }

    @Override
    public int compareTo(Flat other) {
        return this.name.compareTo(other.getName());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flat flat = (Flat) o;
        return Objects.equals(id, flat.id) &&
                Objects.equals(name, flat.name) &&
                Objects.equals(coordinates, flat.coordinates);
    }
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return String.format("Flat[ID:%d, Name:%s, Area:%d, Rooms:%d, Furnish:%s]",
                id, name, area, numberOfRooms, furnish);
    }
}
