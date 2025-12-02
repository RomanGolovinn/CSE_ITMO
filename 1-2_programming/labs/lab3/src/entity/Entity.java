package entity;

import place.Place;

public abstract class Entity {
    String name;
    Place p;

    public Entity(String name, Place p){
        this.name = name;
        this.p = p;
    }
}
