package place;

import entity.Entity;

import java.util.ArrayList;

public abstract class Place {
    String name;
    ArrayList<Entity> EntityList;

    public Place(String name, ArrayList<Entity> el){
        this.name = name;
        this.EntityList = el;
    }

    public String getName() {
        return name;
    }

    public void removeEntity(Entity entity){
        EntityList.remove(entity);
    }

    public void addEntity(Entity entity) {
        EntityList.add(entity);
    }
}
