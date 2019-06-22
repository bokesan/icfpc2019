package icfpc2019;

import java.util.ArrayList;
import java.util.List;

import icfpc2019.pathfinder.Pathfinder;
import icfpc2019.pathfinder.StarNode;

import static icfpc2019.BoosterCode.C;
import static icfpc2019.BoosterCode.R;

public class State {

    private List<BoosterLocation> gridBoosters;
    private Grid grid;
    private List<Point> toVisit;
    private List<Robot> robots = new ArrayList<>();
    private Pathfinder finder;

    public State(Grid grid, Robot robot, List<BoosterLocation> boosters, Pathfinder finder) {
        this.grid = grid;
        this.robots.add(robot);
        this.gridBoosters = boosters;
        this.toVisit = new ArrayList<>();
        this.finder = finder;
        this.toVisit = grid.getFreeSquares();
        removePointsToVisit(robot);
    }

    private void removePointsToVisit(Robot robot) {
        toVisit.remove(robot.position);
        for (Point p : robot.getManipulators()) {
            if (grid.visible(robot.position, p)) {
                toVisit.remove(p);
            }
        }
    }

    public Point getNextPointToVisit(Robot robot) {
        if (toVisit.isEmpty()) return null;
        List<Point> targets = toVisit;
        boolean complexMode = false;
        if (robot.getGatheredBoosters().contains(BoosterCode.C)) {
            //we have a clone booster and want to find a platform
            targets = new ArrayList<>();
            for (BoosterLocation booster : gridBoosters) {
                if (booster.getBoosterCode() == BoosterCode.X) {
                    targets.add(booster.getPoint());
                    complexMode = true;
                }
            }
            if (targets.isEmpty()) targets = toVisit;
        }
        List<Point> teleports = new ArrayList<>();
        for (BoosterLocation booster : gridBoosters) {
            if (booster.getBoosterCode() == R || booster.getBoosterCode() == C) teleports.add(booster.getPoint());
        }
        if (!teleports.isEmpty()) {
            targets = teleports;
            complexMode = true;
        }

        Point best = targets.get(0);
        Point current = robot.position;
        int bestDistance = getBestDistance(best, current, complexMode);
        for (Point p : targets) {
            int distance = getBestDistance(current, p, complexMode);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = p;
            }
        }
        return best;
    }

    private int getBestDistance(Point best, Point current, boolean complexMode) {
        if (complexMode) return finder.findPath(best, current, 0).size();
        return Math.abs(best.getX() - current.getX()) + Math.abs(best.getY() - current.getY());
    }

    public void move(Robot robot, List<StarNode> path) {
        StarNode last = path.get(path.size() - 1);
        for (StarNode point : path) {
            if (!lastPointOpen(last) && !lastPointRelevant(last)) break;
            move(robot, point);
        }
    }

    private boolean lastPointRelevant(StarNode last) {
        for (BoosterLocation booster : gridBoosters) {
            if (booster.getBoosterCode() == BoosterCode.X ||
                booster.getBoosterCode() == R ||
                booster.getBoosterCode() == BoosterCode.C ||
                booster.getBoosterCode() == BoosterCode.B) {
                if (booster.getPoint().equals(Point.of(last.getXPosition(), last.getYPosition()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean lastPointOpen(StarNode last) {
        return toVisit.contains(Point.of(last.getXPosition(), last.getYPosition()));
    }

    public void move(Robot robot, StarNode node) {
        if (robot.direction == Direction.EAST || robot.direction == Direction.WEST) {
            //we are facing left or right and want to turn if we gonna move up or down
            if (node.getYPosition() > robot.position.getY()) {
                turn(robot,robot.direction == Direction.EAST);
            } else if (node.getYPosition() < robot.position.getY()) {
                turn(robot,robot.direction == Direction.WEST);
            }
        } else {
            //we are facing up or down and want to turn if we gonna move left or right
            if (node.getXPosition() > robot.position.getX()) {
                turn(robot,robot.direction == Direction.SOUTH);
            } else if (node.getXPosition() < robot.position.getX()) {
                turn(robot,robot.direction == Direction.NORTH);
            }
        }
        robot.move(node);
        removePointsToVisit(robot);
        collectBooster(robot);
        checkSpawningOpportunity(robot);
    }

    private void checkSpawningOpportunity(Robot robot) {
        if (robot.getGatheredBoosters().contains(BoosterCode.C)) {
            for (BoosterLocation booster : gridBoosters) {
                if (booster.getBoosterCode() == BoosterCode.X && booster.getPoint().equals(robot.position)) {
                    spawnNewRobot(robot);
                }
            }
        }
    }

    private void spawnNewRobot(Robot robot) {
        Robot newBot = new Robot(Point.of(robot.position.getX(), robot.position.getY()));
        robot.useBooster(BoosterCode.C);
        robots.add(newBot);
    }

    private void collectBooster(Robot robot) {
        for (BoosterLocation booster : gridBoosters) {
            if (booster.getBoosterCode() != BoosterCode.X && booster.getPoint().equals(robot.position)) {
                robot.addBooster(booster.getBoosterCode());
                gridBoosters.remove(booster);
                switch (booster.getBoosterCode()) {
                    case R:
                        robot.useBooster(booster.getBoosterCode());
                        finder.addTeleport(robot.position);
                        break;
                    case B:
                        robot.useBooster(BoosterCode.B);
                        break;
                    default:
                        break;
                }
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
