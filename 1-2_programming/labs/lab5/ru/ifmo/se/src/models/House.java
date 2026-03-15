package models;

public class House {
    private String name; //Поле может быть null
    private Integer year; //Поле не может быть null, Значение поля должно быть больше 0
    private long numberOfFloors; //Значение поля должно быть больше 0

    public House(String name, Integer year, long numberOfFloors) {
        this.name = name;
        this.year = year;
        this.numberOfFloors = numberOfFloors;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return "House{name='" + name + "', year=" + year + ", floors=" + numberOfFloors + "}";
    }
}
