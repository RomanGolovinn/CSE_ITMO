package entity;

import place.Place;

public abstract class Entity {
    String name;
    Place p;
    public Place getPlace(){
        return p;
    }

    public Entity(String name, Place p){
        this.name = name;
        this.p = p;
    }

    public String getName(){
        return name;
    }

    public void setPlace(Place p){
        this.p = p;
    }
}
