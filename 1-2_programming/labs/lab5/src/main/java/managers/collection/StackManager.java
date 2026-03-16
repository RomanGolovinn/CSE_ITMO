package main.java.managers.collection;

import main.java.managers.models.Flat;

import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

public class StackManager extends CollectionManager {
    Stack<Flat> flats = new Stack<>();

    @Override
    public void add(Flat flat) {
        flats.push(flat);
    }

    @Override
    public boolean removeById(Long id) {
        return flats.removeIf(f -> f.getId().equals(id));
    }

    @Override
    public void clear() {
        flats.clear();
    }

    @Override
    public Flat getById(Long id) {
        return flats.stream().filter(f -> f.getId().equals(id)).findFirst()
                .orElse(null);
    }

    @Override
    public Collection<Flat> getCollection() {
        return flats;
    }

    @Override
    public void sort() {
        Collections.sort(flats);
    }

    @Override
    public boolean update(Long id, Flat newFlat) {
        for (int i = 0; i < flats.size(); i++) {
            if (flats.get(i).getId().equals(id)) {

                newFlat.setId(id);

                flats.set(i, newFlat);
                return true;
            }
        }
        return false;
    }
}
