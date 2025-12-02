package entity.children;

import entity.Entity;
import place.Place;

public class Plant extends Entity {
    int Size;
    public Plant(String name, Place p){
        super(name, p);
    }

    void Grow(){
        this.Size += 1;
    }

    boolean canLost(){
        if (Size >= 10){
            return true;
        }
        return false;
    }
}
