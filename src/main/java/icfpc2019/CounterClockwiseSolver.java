package icfpc2019;

import icfpc2019.pathfinder.Pathfinder;

import java.util.List;

import static icfpc2019.Action.*;

public class CounterClockwiseSolver implements Solver {

    private Pathfinder finder;
    private Grid grid;
    private State state;
    private List<Robot> robots;

    @Override
    public void init(ProblemDesc problem) {
        grid = Grid.of(problem);
        finder = new Pathfinder();
        finder.initNodes(grid, problem.getBoosters());
        state = new State(grid, problem.getBoosters());
        Robot robot = new Robot(problem.getInitialWorkerLocation());
        robots.add(robot);
        markFieldsWrapped(robot);
    }

    @Override
    public String solve() {

        //collect all manipulators and attach to the right
        //todo first collect manipulators
        //setupManipulators(robot, state);

        //todo do cloning?
        //todo set up teleports?

        Robot robot = robots.get(0);

        while (!state.mapFinished()) {
            //find an empty field next to a wall or wrapped field
            //orient "backwards" with manipulators trailing and pointing away from wall
            setupStartLocation(robot);

            //fill that connected area
            loop: while(true) {
                //if all adjacent fields are filled, break
                for (Point p : robot.position.adjacent()) {
                    if (state.needsWrapping(p)) continue;
                    break loop;
                }

                //if there is no wall/wrap to my right, turn right and move forward

                //if there is a wall in front, turn left

                //otherwise, move forward

            }

        }
        return combineResults();
    }

    private void setupStartLocation(Robot robot) {
        //find the nearest empty field
        List<Point> targets = state.getUnwrappedPoints();
        Point best = targets.get(0);
        int bestDistance = finder.getPathLength(robot.position, best);

        for (Point p : targets) {
            int distance = finder.getPathLength(robot.position, best);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = p;
            }
        }

        List<Point> path = finder.getPath(robot.position, best);
        move(robot, path);
    }

    private void move(Robot robot, List<Point> path) {
        for (Point p : path) {
            //todo do we want to turn, maybe?
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
            default: throw new RuntimeException("Action not implemented: " + action.name());
        }
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
