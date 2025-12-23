package entity.children;

import entity.Entity;
import place.Place;

public final class Plant extends Entity {
    int Size = 0;
    public Plant(String name, Place p){
        super(name, p);
    }

    public void grow(){
        this.Size += 1;
    }

    boolean canLost(){
        return Size >= 10;
    }
    public int getSize(){
        return Size;
    }
}
