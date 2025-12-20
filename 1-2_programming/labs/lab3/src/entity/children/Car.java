package entity.children;

import entity.Entity;
import entity.parts.Direction;
import entity.parts.Door;
import place.Place;

public class Car extends Entity {
    boolean goOut = false;
    Direction dir = Direction.DEGREE_0;
    Door door = new Door(false);
    public Car(String name, Place p){
        super(name, p);
    }

    void turnAround(Direction dir){
        int newDir= dir.getAngle() + 180;
        if (newDir >= 360){
            newDir -= 360;
        }
        this.dir = Direction.fromAngle(newDir);
        System.out.println("Машина развернулась");
    }

    void moveTo(Place out){
        out.EntityList.remove(this);
        System.out.println("Машина уехала из " + out.name);
    }

    void moveTo(Place out, Place in){
        out.EntityList.remove(this);
        in.EntityList.add(this);
        System.out.println("Машина уехала из "+ out.name + " в " + in.name);
    }

    public Door getDoor(){
        return door;
    }

    public void setDoor(Door d){
        this.door = d;
    }

    public boolean isGoOut(){
        return goOut;
    }
}