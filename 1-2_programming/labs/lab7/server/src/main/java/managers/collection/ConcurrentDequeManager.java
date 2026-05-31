package managers.collection;

import models.Flat;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ConcurrentDequeManager extends CollectionManager {
    private final ConcurrentLinkedDeque<Flat> flats = new ConcurrentLinkedDeque<>();
    private final ConcurrentLinkedDeque<Flat> savedFlats = new ConcurrentLinkedDeque<>();

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
        return flats.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<Flat> getCollection() {
        return flats;
    }

    @Override
    public void sort() {
        List<Flat> list = new ArrayList<>(flats);
        Collections.sort(list);
        flats.clear();
        for (int i = list.size() - 1; i >= 0; i--) {
            flats.push(list.get(i));
        }
    }

    @Override
    public boolean update(Long id, Flat newFlat) {
        Flat oldFlat = getById(id);
        if (oldFlat != null) {
            flats.remove(oldFlat);
            newFlat.setId(oldFlat.getId());
            newFlat.setOwnerId(oldFlat.getOwnerId());
            flats.push(newFlat);
            return true;
        }
        return false;
    }

    @Override
    public void setCollection(Collection<Flat> newCollection) {
        flats.clear();
        for (Flat flat : newCollection) {
            flats.add(flat);
        }
    }

    @Override
    public void beginTransaction() {
        if (isTransactionActive) {
            throw new IllegalStateException("Транзакция уже активна.");
        }
        isTransactionActive = true;
        savedFlats.clear();
        savedFlats.addAll(flats);
    }

    @Override
    public void commitTransaction() {
        if (!isTransactionActive) {
            throw new IllegalStateException("Нет активной транзакции.");
        }
        isTransactionActive = false;
        savedFlats.clear();
    }

    @Override
    public void rollbackTransaction() {
        if (!isTransactionActive) {
            throw new IllegalStateException("Нет активной транзакции для отката.");
        }
        isTransactionActive = false;
        flats.clear();
        flats.addAll(savedFlats);
        savedFlats.clear();
    }
}
