package character;

import entity.children.Car;
import place.Place;

abstract public class Character {
    public String name;

    public abstract void walk(Place p);

    void whatchOnCar(Car c){
        if (c.goOut){
            System.out.println(name + "смотрит вслед за машиной");
        }
    }

    void beHappy(){
        System.out.println(name + "рад(а) вернуться в город");
    }

    void comeToPlace(Place p){
        System.out.println(name+ "пришёл(ла) в " + p.name);
    }

    void moveTo(Place out){
        System.out.println(name + "ушёл(ла) из " + out.name);
    }

    void moveTo(Place out, Place in){
        System.out.println(name + "ушёл(ла) из " + in.name + " в " + out.name);
    }

}
