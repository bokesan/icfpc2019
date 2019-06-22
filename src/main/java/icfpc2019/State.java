package icfpc2019;

import java.util.ArrayList;
import java.util.List;

import icfpc2019.pathfinder.Pathfinder;
import icfpc2019.pathfinder.StarNode;

public class State {

    private List<BoosterLocation> gridBoosters;
    private Grid grid;
    private List<Point> toVisit;
    private Robot robot;
    private Pathfinder finder;

    public State(Grid grid, Robot robot, List<BoosterLocation> boosters, Pathfinder finder) {
        this.grid = grid;
        this.robot = robot;
        this.gridBoosters = boosters;
        this.toVisit = new ArrayList<>();
        this.finder = finder;
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

    public void move(List<StarNode> path) {
        StarNode last = path.get(path.size() - 1);
        for (StarNode node : path) {
            if (!toVisit.contains(Point.of(last.getXPosition(), last.getYPosition()))) break;
            move(node);
        }
    }

    public void move(StarNode node) {
        if (robot.direction == Direction.EAST || robot.direction == Direction.WEST) {
            //we are facing left or right and want to turn if we gonna move up or down
            if (node.getYPosition() > robot.position.getY()) {
                turn(robot.direction == Direction.EAST);
            } else if (node.getYPosition() < robot.position.getY()) {
                turn(robot.direction == Direction.WEST);
            }
        } else {
            //we are facing up or down and want to turn if we gonna move left or right
            if (node.getXPosition() > robot.position.getX()) {
                turn(robot.direction == Direction.SOUTH);
            } else if (node.getXPosition() < robot.position.getX()) {
                turn(robot.direction == Direction.NORTH);
            }
        }
        robot.move(node);
        removePointsToVisit();
        collectBooster();
    }

    private void collectBooster() {
        for (BoosterLocation booster : gridBoosters) {
            if (booster.getPoint().equals(robot.position)) {
                robot.addBooster(booster.getBoosterCode());
                gridBoosters.remove(booster);
                if(booster.getBoosterCode() == BoosterCode.R){                    
                    robot.useBooster(booster.getBoosterCode());
                    finder.addTeleport(robot.position);
                }
                    
                break;
            }
        }
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

    public void turn(boolean left) {
        if (left) {
            robot.spin(Actions.Q);
        } else {
            robot.spin(Actions.E);
        }
        removePointsToVisit();
    }
}
