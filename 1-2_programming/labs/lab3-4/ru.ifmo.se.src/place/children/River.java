package place.children;

import entity.Entity;
import entity.children.Plant;
import place.Place;

import java.util.ArrayList;

public final class River extends Place {
    ArrayList<Plant> plants;
    public River(String name, ArrayList<Plant> plants){
        ArrayList<Entity> el = new ArrayList<>();
        super(name, el);
        this.plants = plants;
    }

    public void addPlat(Plant p){
        plants.add(p);
    }

    public ArrayList<Plant> getPlants(){
        return plants;
    }
}
