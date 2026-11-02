package ru.itmo.lab2.core;

public final class PointRequest {
    private final double x;
    private final double y;
    private final double r;

    public PointRequest(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
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
}
