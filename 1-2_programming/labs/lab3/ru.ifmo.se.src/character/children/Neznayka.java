package character.children;

import character.Character;
import character.clother.Color;
import character.clother.Gloves;
import entity.children.Car;
import entity.parts.Door;
import exeptions.LostException;
import place.Place;
import place.children.River;

final public class Neznayka extends Character {
    Gloves gloves;
    public Neznayka(String name, Gloves g){
        super(name);
        this.name = "Незнайка";
        this.gloves = g;
    }

    @Override
    public void walk(Place p) throws LostException {
        checkLostException(p);
        Color color = this.gloves.c();
        System.out.println(name + " гуляет по " + p.getName() + " заложив за спину руки в " + color + " перчатках");
        walkOnStreets(p);
    }

    public void closeCar(Car c){
        Door door = c.getDoor();

        door.changeStatus();

        c.setDoor(door);
        c.moveTo(c.getPlace());
    }
}
