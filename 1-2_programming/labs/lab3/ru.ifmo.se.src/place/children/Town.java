package place.children;

import entity.Entity;
import place.Place;
import place.Street;

import java.util.ArrayList;

public final class Town extends Place {
    ArrayList<Street> streets;
    public Town(String name, ArrayList<Street> streets){
        ArrayList<Entity> el = new ArrayList<>();
        super(name, el);
        this.streets = streets;
    }

    public ArrayList<Street> getStreets(){
        return streets;
    }
}
