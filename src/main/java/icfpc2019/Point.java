package icfpc2019;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Point {

    private static final Point ORIGIN = new Point(0,0);

    private final int x;
    private final int y;

    private Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point origin() {
        return ORIGIN;
    }

    public static Point of(int x, int y) {
        return new Point(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point left() {
        return of(x - 1, y);
    }

    public Point right() {
        return of(x + 1, y);
    }

    public Point up() {
        return of(x, y + 1);
    }

    public Point down() {
        return of(x, y - 1);
    }

    public Point translate(int dx, int dy) {
        return of(x + dx, y + dy);
    }

    public Point translate(Point offset) {
        return of(x + offset.x, y + offset.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    List<Point> adjacent() {
        return new ArrayList<Point>() {{add(up()); add(down()); add(left()); add(right());}};
    }
}
