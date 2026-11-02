package ru.itmo.lab2.core;

import java.time.Instant;

public final class PointService {
    private final PointHitChecker hitChecker;
    private final PointHistory history;

    public PointService(PointHitChecker hitChecker, PointHistory history) {
        this.hitChecker = hitChecker;
        this.history = history;
    }

    public PointResult check(PointRequest request) {
        PointResult result = new PointResult(request, hitChecker.contains(request), Instant.now());
        history.add(result);
        return result;
    }

    public PointHistory getHistory() {
        return history;
    }
}
