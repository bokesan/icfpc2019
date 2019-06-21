package icfpc2019;

import java.util.ArrayList;
import java.util.List;

public class State {

    private List<BoosterLocation> gridBoosters;
    private Grid grid;
    private List<Point> toVisit;
    private Robot robot;

    public State(Grid grid, Robot robot, List<BoosterLocation> boosters) {
        this.grid = grid;
        this.robot = robot;
        this.gridBoosters = boosters;
        this.toVisit = new ArrayList<>();
        addPointsToVisit();
        removePointsToVisit();
    }

    private void removePointsToVisit() {
        List<Point> touched = new ArrayList<>();
        touched.add(robot.position);
        touched.addAll(robot.getManipulators());
        for (Point p : touched) {
            toVisit.remove(p);
        }
    }

    private void addPointsToVisit() {
        for (int x = 0; x < grid.max.getX(); x++) {
            for (int y = 0; y < grid.max.getY(); y++) {
                if (grid.isFree(Point.of(x, y))) {
                    toVisit.add(Point.of(x, y));
                }
            }
        }
    }

    public Point getNextPointToVisit() {
        if (toVisit.isEmpty()) return null;
        Point best = toVisit.get(0);
        Point current = robot.position;
        int bestDistance = Math.abs(best.getX() - current.getX()) + Math.abs(best.getY() - current.getY());
        for (Point p : toVisit) {
            int distance = Math.abs(current.getX() - p.getX()) + Math.abs(current.getY() - p.getY());
            if (distance < bestDistance) {
                bestDistance = distance;
                best = p;
            }
        }
        return best;
    }

    public void move(List<Point> path) {
        for (Point point : path) move(point);
    }

    public void move(Point point) {
        robot.move(point);
        removePointsToVisit();
    }

    public boolean mapFinished() {
        return toVisit.isEmpty();
    }

    public String getResult() {
        return robot.getActionLog();
    }

    public Point getCurrentPosition() {
        return robot.position;
    }
}
