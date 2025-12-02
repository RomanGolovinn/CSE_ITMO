package character.children;

import character.Character;
import character.clother.Color;
import character.clother.Gloves;
import entity.children.Car;
import place.Place;

public class Neznayka extends Character {
    Gloves g;
    public Neznayka(Gloves g){
        this.name = "Незнайка";
        this.g = g;
    }

    public void walk(Place p){
        Color color = this.g.c();
        System.out.println(name + " гуляет по " + p.name + " заложив за спину руки в " + color + " перчатках");
    }

    void closeCar(Car c){
        c.d.changeStatus();

    }
}
