package ru.itmo.lab2.core;

import java.time.Instant;

public final class PointResult {
    private final double x;
    private final double y;
    private final double r;
    private final boolean hit;
    private final Instant checkedAt;

    public PointResult(PointRequest request, boolean hit, Instant checkedAt) {
        this.x = request.getX();
        this.y = request.getY();
        this.r = request.getR();
        this.hit = hit;
        this.checkedAt = checkedAt;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public boolean isHit() {
        return hit;
    }

    public Instant getCheckedAt() {
        return checkedAt;
    }
}
