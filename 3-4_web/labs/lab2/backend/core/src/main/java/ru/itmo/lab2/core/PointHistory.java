package ru.itmo.lab2.core;

import java.util.List;

public interface PointHistory {
    void add(PointResult result);

    List<PointResult> getAll();

    void clear();
}
