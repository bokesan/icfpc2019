package icfpc2019;

import icfpc2019.pathfinder.Pathfinder;

import java.util.ArrayList;
import java.util.List;

import static icfpc2019.Action.*;
import static icfpc2019.Direction.SOUTH;

public class CounterClockwiseSolver implements Solver {

    private Pathfinder finder;
    private Grid grid;
    private State state;
    private List<Robot> robots = new ArrayList<>();
    private int cutPathCounter = 0;

    @Override
    public void init(ProblemDesc problem) {
        grid = Grid.of(problem);
        finder = new Pathfinder();
        finder.initNodes(grid);
        state = new State(grid, problem.getBoosters());
        Robot robot = new Robot(problem.getInitialWorkerLocation());
        robots.add(robot);
        markFieldsWrapped(robot);
    }

    @Override
    public String solve() {
        Robot robot = robots.get(0);

        //collect all manipulators and attach to the right
        setupManipulators(robot);

        //todo do cloning?
        //todo set up teleports?

        while (!state.mapFinished()) {
            //find an empty field next to a wall or wrapped field
            //orient "backwards" with manipulators trailing and pointing away from wall
            setupStartLocation(robot);
            //System.out.println("Set Up Position");

            //fill that connected area
            //if all adjacent fields are filled, break
            while (hasFreeNeighbours(robot)) {
                //if there is no wall/wrap to my right, turn right and move forward
                Point right = getMyRight(robot);
                if (state.needsWrapping(right)) {
                    //System.out.println("Turning right");
                    if (state.needsWrapping(getDoubleRight(robot))) {
                        performAction(E, robot);
                    }
                    move(robot, right);
                    continue;
                }

                //if there is a need for wrap ahead, move on
                Point front = getMyFront(robot);
                if (state.needsWrapping(front)) {
                    //System.out.println("Moving forward");
                    move(robot, front);
                    continue;
                }

                //otherwise, turn left
                performAction(Q, robot);
                //System.out.println("Turning left");
            }
        }
        return combineResults();
    }

    private Point getDoubleRight(Robot robot) {
        // we move with our manipulators trailing behind, so this is kinda flipped
        switch (robot.direction) {
            case SOUTH: return robot.position.right().right();
            case NORTH: return robot.position.left().left();
            case WEST:  return robot.position.down().down();
            case EAST:  return robot.position.up().up();
            default: throw new RuntimeException("Invalid direction: " + robot.direction.name());
        }
    }

    private void setupManipulators(Robot robot) {
        List<Point> manipulatorBoosters = state.getBoosterLocations(BoosterCode.B);
        while (!manipulatorBoosters.isEmpty()) {
            Point next = getNearestPoint(manipulatorBoosters, robot.position);
            moveUntilThere(robot, next);
            performAction(B, robot);
            manipulatorBoosters = state.getBoosterLocations(BoosterCode.B);
        }
    }

    private Point getNearestPoint(List<Point> points, Point start) {
        if (points.isEmpty()) return null;
        Point best = points.get(0);
        int distance = finder.getPathLength(start, best);
        points.remove(0);
        for (Point p : points) {
            int d = finder.getPathLength(start, p);
            if (d < distance) {
                best = p;
                distance = d;
            }
        }
        return best;
    }

    private boolean hasFreeNeighbours(Robot robot) {
        boolean trapped = true;
        for (Point p : robot.position.adjacent()) {
            if (state.needsWrapping(p)) {
                trapped = false;
                break;
            }
        }
        return !trapped;
    }

    private Point getMyFront(Robot robot) {
        // we move with our manipulators trailing behind, so this is kinda flipped
        switch (robot.direction) {
            case SOUTH: return robot.position.up();
            case NORTH: return robot.position.down();
            case WEST:  return robot.position.right();
            case EAST:  return robot.position.left();
            default: throw new RuntimeException("Invalid direction: " + robot.direction.name());
        }
    }

    private Point getMyRight(Robot robot) {
        // we move with our manipulators trailing behind, so this is kinda flipped
        switch (robot.direction) {
            case SOUTH: return robot.position.right();
            case NORTH: return robot.position.left();
            case WEST:  return robot.position.down();
            case EAST:  return robot.position.up();
            default: throw new RuntimeException("Invalid direction: " + robot.direction.name());
        }
    }

    private void setupStartLocation(Robot robot) {
        //find the nearest empty field
        List<Point> targets = state.getUnwrappedPoints();
        Point best = targets.get(0);
        int bestDistance = getManhattanDistance(robot.position, best);

        for (Point p : targets) {
            int distance = getManhattanDistance(robot.position, p);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = p;
            }
        }

        List<Point> path = finder.getPath(robot.position, best);
        moveUntilFree(robot, path);
    }

    private int getManhattanDistance(Point from, Point to) {
        return Math.abs(from.getX() - to.getX()) +
                Math.abs(from.getY() - to.getY());
    }

    private void moveUntilFree(Robot robot, List<Point> path) {
        int maxLength = 20 + cutPathCounter;
        int steps = 0;
        for (Point p : path) {
            move(robot, p);
            steps++;
            //as soon as we find an area to fill, we go for it
            if (hasFreeNeighbours(robot)) {
                cutPathCounter = 0;
                break;
            }
            if (steps > maxLength) {
                cutPathCounter += 10;
                break;
            }
        }
    }

    private void moveUntilThere(Robot robot, Point target) {
        List<Point> path = finder.getPath(robot.position, target);
        for (Point p : path) {
            move(robot, p);
        }
    }

    private void move(Robot robot, Point target) {
        Direction direction = Direction.of(robot.position, target);
        if (direction == null) throw new RuntimeException("Invalid move: " + robot.position + " to " + target);
        switch (direction) {
            case EAST:  performAction(D, robot); break;
            case WEST:  performAction(A, robot); break;
            case NORTH: performAction(W, robot); break;
            case SOUTH: performAction(S, robot); break;
            default: throw new RuntimeException("Invalid direction: " + direction.name());
        }
    }

    private void performAction(Action action, Robot robot) {
        switch (action) {
            case W: //FALLTHROUGH
            case A: //FALLTHROUGH
            case S: //FALLTHROUGH
            case D: robot.singleStep(action); //fixme: this entire stuff might happen twice if the robot has fast wheels
                markFieldsWrapped(robot);
                state.pickBoosterUp(robot.position);
                break;
            case Q: //FALLTHROUGH
            case E: robot.turn(action);
                markFieldsWrapped(robot);
                break;
            case B: state.removeBooster(BoosterCode.B);
                attachManipulator(robot);
                markFieldsWrapped(robot);
                break;
            default: throw new RuntimeException("Action not implemented: " + action.name());
        }
    }

    private void attachManipulator(Robot robot) {
        List<Point> manipulators = robot.getManipulators();
        Point newPosition;
        switch (robot.direction) {
            case EAST:
                for (int offs = 2; ; offs++) {
                    newPosition = robot.position.translate(1, -offs);
                    if (!manipulators.contains(newPosition))
                        break;
                }
                break;
            case WEST:
                for (int offs = 2; ; offs++) {
                    newPosition = robot.position.translate(-1, offs);
                    if (!manipulators.contains(newPosition))
                        break;
                }
                break;
            case NORTH:
                for (int offs = 2; ; offs++) {
                    newPosition = robot.position.translate(offs, 1);
                    if (!manipulators.contains(newPosition))
                        break;
                }
                break;
            case SOUTH:
                for (int offs = 2; ; offs++) {
                    newPosition = robot.position.translate(-offs, -1);
                    if (!manipulators.contains(newPosition))
                        break;
                }
                break;
            default:
                throw new AssertionError();
        }
        robot.attachManipulator(newPosition);
    }

    private void markFieldsWrapped(Robot robot) {
        for (Point p : robot.getManipulators()) {
            if (grid.visible(p, robot.position)) state.wrap(p);
        }
    }

    private String combineResults() {
        StringBuilder builder = new StringBuilder();
        builder.append(robots.get(0).getActionLog());
        for (int i = 1; i < robots.size(); i++) {
            builder.append("#");
            builder.append(robots.get(i).getActionLog());
        }
        return builder.toString();
    }
}
