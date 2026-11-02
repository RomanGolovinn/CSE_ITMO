package ru.itmo.lab2.core;

public final class LabAreaChecker implements PointHitChecker {
    @Override
    public boolean contains(PointRequest point) {
        double x = point.getX();
        double y = point.getY();
        double r = point.getR();

        boolean inTriangle = x >= -r && x <= 0 && y >= 0
                && y <= (x + r) / 2;

        boolean inQuarterCircle = x >= -r / 2 && x <= 0 && y <= 0
                && square(x) + square(y) <= square(r / 2);

        boolean inRectangle = x >= 0 && x <= r / 2 && y >= -r && y <= 0;

        return inTriangle || inQuarterCircle || inRectangle;
    }

    private double square(double value) {
        return value * value;
    }
}
