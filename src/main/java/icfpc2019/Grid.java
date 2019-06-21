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
    public boolean[][] getFields(){
        return fields;
    }
}
