package ru.itmo.lab2.core;

import java.util.List;

public final class PointValidator {
    private static final List<Double> ALLOWED_RADII = List.of(1.0, 1.5, 2.0, 2.5, 3.0);

    private PointValidator() {
    }

    public static PointRequest validate(String rawX, String rawY, String rawR) {
        double x = parseFiniteNumber("X", rawX);
        double y = parseFiniteNumber("Y", rawY);
        double r = parseFiniteNumber("R", rawR);

        if (x < -4 || x > 4 || x != Math.rint(x)) {
            throw new IllegalArgumentException("X must be an integer from -4 to 4");
        }
        if (y < -5 || y > 5) {
            throw new IllegalArgumentException("Y must be between -5 and 5, inclusive");
        }
        if (!ALLOWED_RADII.contains(r)) {
            throw new IllegalArgumentException("R must be one of 1, 1.5, 2, 2.5, 3");
        }

        return new PointRequest(x, y, r);
    }

    private static double parseFiniteNumber(String name, String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " is required");
        }

        final double value;
        try {
            value = Double.parseDouble(rawValue.trim().replace(',', '.'));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(name + " must be a number");
        }

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(name + " must be finite");
        }
        return value;
    }
}
