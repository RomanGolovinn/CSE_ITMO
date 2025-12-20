package entity.children;

import entity.Entity;
import entity.parts.Direction;
import entity.parts.Door;
import interfaces.Mobile;
import place.Place;

public final class Car extends Entity implements Mobile {
    boolean goOut = false;
    Direction dir = Direction.DEGREE_0;
    Door door = new Door(true);
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

    public void moveTo(Place out){
        turnAround(Direction.DEGREE_0);
        out.removeEntity(this);
        System.out.println("Машина уехала из " + out.getName());
    }

    public void moveTo(Place out, Place in){
        out.removeEntity(this);
        in.addEntity(this);
        System.out.println("Машина уехала из "+ out.getName() + " в " + in.getName());
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