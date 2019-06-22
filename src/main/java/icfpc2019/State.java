package icfpc2019;

import java.util.ArrayList;
import java.util.List;

public class State {

    private List<BoosterLocation> gridBoosters;
    private Grid grid;
    private List<Point> toVisit;
    private List<Robot> robots = new ArrayList<>();

    public State(Grid grid, Robot robot, List<BoosterLocation> boosters) {
        this.grid = grid;
        this.robots.add(robot);
        this.gridBoosters = boosters;
        this.toVisit = new ArrayList<>();
        addPointsToVisit();
        removePointsToVisit(robot);
    }

    private void removePointsToVisit(Robot robot) {
        List<Point> touched = new ArrayList<>();
        touched.add(robot.position);
        // FIXME: check visibility
        touched.addAll(robot.getManipulators());
        for (Point p : touched) {
            toVisit.remove(p);
        }
    }

    private void addPointsToVisit() {
        for (int x = 0; x < grid.max.getX(); x++) {
            for (int y = 0; y < grid.max.getY(); y++) {
                if (grid.isFree(x, y)) {
                    toVisit.add(Point.of(x, y));
                }
            }
        }
    }

    public Point getNextPointToVisit(Robot robot) {
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

    public void move(Robot robot, List<Point> path) {
        Point last = path.get(path.size() - 1);
        for (Point point : path) {
            if (!toVisit.contains(last)) break;
            move(robot, point);
        }
    }

    private void move(Robot robot, Point point) {
        if (robot.direction == Direction.EAST || robot.direction == Direction.WEST) {
            //we are facing left or right and want to turn if we gonna move up or down
            if (point.getY() > robot.position.getY()) {
                turn(robot,robot.direction == Direction.EAST);
            } else if (point.getY() < robot.position.getY()) {
                turn(robot,robot.direction == Direction.WEST);
            }
        } else {
            //we are facing up or down and want to turn if we gonna move left or right
            if (point.getX() > robot.position.getX()) {
                turn(robot,robot.direction == Direction.SOUTH);
            } else if (point.getX() < robot.position.getX()) {
                turn(robot,robot.direction == Direction.NORTH);
            }
        }
        robot.move(point);
        removePointsToVisit(robot);
        collectBooster(robot);
    }

    private void collectBooster(Robot robot) {
        for (BoosterLocation booster : gridBoosters) {
            if (booster.getBoosterCode() != BoosterCode.X && booster.getPoint().equals(robot.position)) {
                robot.addBooster(booster.getBoosterCode());
                gridBoosters.remove(booster);
                break;
            }
        }
    }

    public boolean mapFinished() {
        return toVisit.isEmpty();
    }

    public String getResult(Robot robot) {
        return robot.getActionLog();
    }

    public Point getCurrentPosition(Robot robot) {
        return robot.position;
    }

    public void turn(Robot robot, boolean left) {
        if (left) {
            robot.spin(Actions.Q);
        } else {
            robot.spin(Actions.E);
        }
        removePointsToVisit(robot);
    }

    public int getNumRobots() {
        return robots.size();
    }

    public Robot getRobot(int index) {
        return robots.get(index);
    }
}
