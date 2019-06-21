package icfpc2019;

import java.util.Objects;

public class Line {

    private final Point p1;
    private final Point p2;

    private Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public static Line of(Point p1, Point p2) {
        if (p1.getY() == p2.getY()) {
            if (p1.getX() <= p2.getX()) {
                return new Line(p1, p2);
            } else {
                return new Line(p2, p1);
            }
        } else {
            if (p1.getY() <= p2.getY()) {
                return new Line(p1, p2);
            } else {
                return new Line(p2, p1);
            }
        }
    }

    public Point getStart() {
        return p1;
    }

    public Point getEnd() {
        return p2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return (p1.equals(line.p1) && p2.equals(line.p2)) ||
                (p1.equals(line.p2) && p2.equals(line.p1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2);
    }

    public boolean isHorizontal() {
        return p1.getY() == p2.getY();
    }

    public String toString() {
        return "(" + p1.getX() + "," + p1.getY() + ") - (" + p2.getX() + "," + p2.getY() + ")";
    }
}
