package icfpc2019;

import java.util.List;

public class Grid {

    private final boolean[][] fields;
    private final Point min;
    private final Point max;

    private Grid(boolean[][] fields, Point min, Point max) {
        this.fields = fields;
        this.min = min;
        this.max = max;
    }

    public Grid of(ProblemDesc problemDesc) {
        boolean[][] fields = new boolean[problemDesc.getMax().getX()][problemDesc.getMax().getY()];
        setSquares(fields, true, problemDesc.getMap());
        for (List<Point> shape : problemDesc.getObstacles()) {
            setSquares(fields, false, shape);
        }
        return new Grid(fields, problemDesc.getMin(), problemDesc.getMax());
    }

    private void setSquares(boolean[][] fields, boolean free, List<Point> shape) {
        //TODO
    }

    public boolean isFree(Point p) {
        if (p.getX() > max.getX() || p.getX() < min.getX() || p.getY() > max.getY() || p.getY() < min.getY()) {
            return false;
        } else {
            return fields[p.getX()][p.getY()];
        }
    }
}
