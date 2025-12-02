package entity.parts;

public enum Direction {
    DEGREE_0(0),
    DEGREE_30(30),
    DEGREE_60(60),
    DEGREE_90(90),
    DEGREE_120(120),
    DEGREE_150(150),
    DEGREE_180(180),
    DEGREE_210(210),
    DEGREE_240(240),
    DEGREE_270(270),
    DEGREE_300(300),
    DEGREE_330(330);

    private final int angle;

    Direction(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public static Direction fromAngle(int angle) {
        for (Direction d : Direction.values()) {
            if (d.angle == angle) {
                return d;
            }
        }
        throw new IllegalArgumentException("Неверный угол: " + angle);
    }
}

