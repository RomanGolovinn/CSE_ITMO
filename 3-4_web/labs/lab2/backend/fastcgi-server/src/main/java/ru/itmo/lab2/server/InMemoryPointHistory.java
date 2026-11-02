package ru.itmo.lab2.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import ru.itmo.lab2.core.PointHistory;
import ru.itmo.lab2.core.PointResult;

public final class InMemoryPointHistory implements PointHistory {
    private final ConcurrentLinkedDeque<PointResult> results = new ConcurrentLinkedDeque<>();

    @Override
    public void add(PointResult result) {
        results.addFirst(result);
    }

    @Override
    public List<PointResult> getAll() {
        return new ArrayList<>(results);
    }

    @Override
    public void clear() {
        results.clear();
    }
}
