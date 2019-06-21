package icfpc2019;

import java.util.Objects;

public class BoosterLocation {

    private final BoosterCode boosterCode;

    private final Point point;

    public BoosterLocation(BoosterCode code, Point point) {
        this.boosterCode = code;
        this.point = point;
    }

    public BoosterCode getBoosterCode() {
        return boosterCode;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoosterLocation that = (BoosterLocation) o;
        return boosterCode == that.boosterCode &&
                point.equals(that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boosterCode, point);
    }
}
