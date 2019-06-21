package icfpc2019;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

import java.util.ArrayList;
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

    public static Grid of(ProblemDesc problemDesc) {
        boolean[][] fields = new boolean[problemDesc.getMax().getX()][problemDesc.getMax().getY()];
        setSquares(fields, problemDesc.getMap(), problemDesc.getObstacles());
        return new Grid(fields, problemDesc.getMin(), problemDesc.getMax());
    }
    public static Grid of(int xMax, int yMax, int xGapPosition,int yGapPosition){

        boolean[][] fields = new boolean[xMax][yMax];
        for(int x = 0; x < xMax; x++){
            for(int y = 0; y < yMax; y++){
                if(x == xGapPosition && y != yGapPosition){
                    fields[x][y] = false;
                }else{
                    fields[x][y] = true;
                }

            }
        }
        return new Grid(fields, Point.of(0, 0), Point.of(xMax, yMax));
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

/*    private static void setSquares(boolean[][] fields, boolean isFree, List<Point> shape) {
        OrthoPolygonDecomposer decomposer = new OrthoPolygonDecomposer();
        Coordinate[] coordinates = new Coordinate[shape.size() + 1];
        for (int i = 0; i < shape.size(); i++) {
            coordinates[i] = new Coordinate(shape.get(i).getX(), shape.get(i).getY());
        }
        coordinates[shape.size()] = coordinates[0];
        GeometryFactory factory = new GeometryFactory();
        LinearRing shell = new LinearRing(new CoordinateArraySequence(coordinates, 2), factory);
        Geometry poly = new Polygon(shell, null, factory);
        List<Polygon> rectangles = decomposer.decompose(poly);

        for (Polygon rectangle : rectangles) {
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = 0;
            int maxY = 0;
            for (Coordinate coord : rectangle.getExteriorRing().getCoordinates()) {
                int x = (int) coord.x;
                int y = (int) coord.y;
                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x);
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
            }
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    fields[x][y] = isFree;
                }
            }
        }
    }*/

    public boolean isFree(Point p) {
        if (p.getX() > max.getX() || p.getX() < min.getX() || p.getY() > max.getY() || p.getY() < min.getY()) {
            return false;
        } else {
            return fields[p.getX()][p.getY()];
        }
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
}
