package icfpc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grid {

    /** If true the coordinate is free, if false it is an obstacle or outside the map. */
    private final boolean[][] fields;
    public final Point min;
    public final Point max;

    private Grid(boolean[][] fields, Point min, Point max) {
        this.fields = fields;
        this.min = min;
        this.max = max;
    }

    public static Grid of(ProblemDesc problemDesc) {
        boolean[][] fields = new boolean[problemDesc.getMax().getX()][problemDesc.getMax().getY()];
        setSquares(fields, problemDesc.getMap(), problemDesc.getObstacles());
        return new Grid(fields, problemDesc.getMin(), problemDesc.getMax());
    }

    public static Grid of(int xMax, int yMax, Point... obstacles) {
        boolean[][] fields = new boolean[xMax][yMax];
        for (int x = 0; x < xMax; x++) {
            Arrays.fill(fields[x], true);
        }
        for (Point p : obstacles) {
            fields[p.getX()][p.getY()] = false;
        }
        return new Grid(fields, Point.origin(), Point.of(xMax, yMax));
    }

    private static void setSquares(boolean[][] fields, List<Point> shape, List<List<Point>> shapes) {
        List<List<Point>> allShapes = new ArrayList<>();
        allShapes.add(shape);
        allShapes.addAll(shapes);
        List<Line> lines = linesFromShapes(allShapes);
        for (int x = 0; x < fields.length; x++) {
            for (int y = 0; y < fields[0].length; y++) {
                fields[x][y] = calculateIsFree(x, y, lines);
            }
        }
    }

    private static boolean calculateIsFree(int x, int y, List<Line> lines) {
        int up = 0;
        int right = 0;
        for (Line line : lines) {
            if (line.isHorizontal()) {
                if (line.getStart().getX() <= x && line.getEnd().getX() > x && line.getStart().getY() > y) up++;
            } else {
                if (line.getStart().getY() <= y && line.getEnd().getY() > y && line.getStart().getX() > x) right++;
            }
        }
        //System.out.println("up: " + up + " - right: " + right);
        return (up % 2 == 1) && (right % 2 == 1);
    }

    private static List<Line> linesFromShapes(List<List<Point>> shapes) {
        List<Line> lines = new ArrayList<>();
        for (List<Point> shape : shapes) {
            for (int i = 1; i < shape.size(); i++) {
                lines.add(Line.of(shape.get(i - 1), shape.get(i)));
                //System.out.println(Line.of(shape.get(i - 1), shape.get(i)));
            }
            lines.add(Line.of(shape.get(0), shape.get(shape.size() - 1)));
            //System.out.println(Line.of(shape.get(0), shape.get(shape.size() - 1)));
        }
        return lines;
    }

    public boolean isFree(int x, int y) {
        if (x >= max.getX() || x < min.getX() || y >= max.getY() || y < min.getY()) {
            return false;
        } else {
            return fields[x][y];
        }
    }

    public boolean isFree(Point p) {
        return isFree(p.getX(), p.getY());
    }

    public boolean[][] getFields(){
        return fields;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int y = fields[0].length - 1; y >= 0; y--) {
            for (int x = 0; x < fields.length; x++) {
                if (fields[x][y]) {
                    builder.append("o");
                } else {
                    builder.append("x");
                }
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    public List<Point> getFreeSquares() {
        List<Point> result = new ArrayList<>((max.getX() - min.getX()) * (max.getY() - min.getY()) / 2);
        for (int x = min.getX(); x < max.getX(); x++) {
            for (int y = min.getY(); y < max.getY(); y++) {
                if (fields[x][y]) {
                    result.add(Point.of(x, y));
                }
            }
        }
        return result;
    }

    public boolean visible(Point p1, Point p2) {
        if (!(isFree(p1) && isFree(p2))) {
            return false;
        }
        if (p1.equals(p2)) {
            return true;
        }

        int dx = p2.getX() - p1.getX();
        int dy = p2.getY() - p1.getY();


        if (dy == 1) {
            if (dx == 2 && !(isFree(p1.right()) && isFree(p1.right().up())))
                return false;
            if (dx == -2 && !(isFree(p1.left()) && isFree(p1.left().up())))
                return false;

            if (dx == 3 && !(isFree(p1.right()) && isFree(p1.translate(2, 1))))
                return false;
            if (dx == -3 && !(isFree(p1.left()) && isFree(p1.translate(-2, 1))))
                return false;

        }
        else if (dy == -1) {
            if (dx == 2 && !(isFree(p1.right()) && isFree(p1.right().down())))
                return false;
            if (dx == -2 && !(isFree(p1.left()) && isFree(p1.left().down())))
                return false;

            if (dx == 3 && !(isFree(p1.right()) && isFree(p1.translate(2, -1))))
                return false;
            if (dx == -3 && !(isFree(p1.left()) && isFree(p1.translate(-2, -1))))
                return false;

        }
        else if (dx == 1) {
            if (dy == 2 && !(isFree(p1.up()) && isFree(p1.up().right())))
                return false;
            if (dy == -2 && !(isFree(p1.down()) && isFree(p1.down().right())))
                return false;
            if (dy == 3 && !(isFree(p1.up()) && isFree(p1.translate(1, 2))))
                return false;
            if (dy == -3 && !(isFree(p1.down()) && isFree(p1.translate(1, -2))))
                return false;
        } else if (dx == -1) {
            if (dy == 2 && !(isFree(p1.up()) && isFree(p1.up().left())))
                return false;
            if (dy == -2 && !(isFree(p1.down()) && isFree(p1.down().left())))
                return false;
            if (dy == 3 && !(isFree(p1.up()) && isFree(p1.translate(-1, 2))))
                return false;
            if (dy == -3 && !(isFree(p1.down()) && isFree(p1.translate(-1, -2))))
                return false;
        } else {
            throw new RuntimeException("should not happen with the way we place manipulators: " + dx + "," + dy);
        }
        return true;
    }


    /**
     * Are the centers of the squares denoted by p1 and p2 visible from each other?
     */
    public boolean visibleX(Point p1, Point p2) {
        if (!(isFree(p1) && isFree(p2))) {
            return false;
        }
        if (p1.equals(p2)) {
            return true;
        }
        int dx = p2.getX() - p1.getX();
        int dy = p2.getY() - p1.getY();
        int nx = Math.abs(dx);
        int ny = Math.abs(dy);
        if (nx > ny) {
            // move in x dir
            int xstep = (int) Math.signum(dx);
            double ystep = (double) dy / nx;
            for (int offs = 1; offs < nx; offs++) {
                int x = p1.getX() + offs * xstep;
                double y = p1.getY() + 0.5 + (offs - 0.5 * xstep) * ystep;
                if (!isFree(x, (int) y)) {
                    return false;
                }
            }
        } else {
            // move in y dir
            int ystep = (int) Math.signum(dy);
            double xstep = (double) dx / ny;
            for (int offs = 1; offs < ny; offs++) {
                int y = p1.getY() + offs * ystep;
                double x = p1.getX() + 0.5 + (offs - 0.5 * ystep) * xstep;
                if (!isFree((int) x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

}
