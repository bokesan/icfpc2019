package icfpc2019;

import icfpc2019.pathfinder.Pathfinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static icfpc2019.Action.*;

public class CounterClockwiseSolverWithFallbackTarget implements Solver {

    private static final boolean DISTRIBUTE_EXTENSIONS_EVENLY = true;
    private static final boolean USE_TELEPORT = true;

    private Pathfinder finder;
    private Grid grid;
    private State state;
    private List<Robot> robots = new ArrayList<>();    
    private int cutPathCounter = 0;
    private int extensionsPerBot = Integer.MAX_VALUE;
    private Point middle;
    @Override
    public void init(ProblemDesc problem) {
        grid = Grid.of(problem);
        middle = Point.of(grid.getFields().length/2, grid.getFields()[0].length/2);
        finder = new Pathfinder(grid);
        state = new State(grid, problem.getBoosters());
        Robot robot = new Robot(problem.getInitialWorkerLocation(), true);
        robots.add(robot);
        markFieldsWrapped(robot);
    }
    @Override
    public void init(ProblemDesc problem, String shoppinglist){
        grid = Grid.of(problem);
        middle = Point.of(grid.getFields().length/2, grid.getFields()[0].length/2);
        finder = new Pathfinder(grid);
        state = new State(grid, problem.getBoosters(), shoppinglist);
        Robot robot = new Robot(problem.getInitialWorkerLocation(), true);
        robots.add(robot);
        markFieldsWrapped(robot);        
    }

    @Override
    public List<ActionSequence> solve() {
        if (DISTRIBUTE_EXTENSIONS_EVENLY) {
            int numExtensions = state.getBoosterLocations(BoosterCode.B).size() + state.getNumAvailableBooster(BoosterCode.B);
            double numRobots = 1 + state.getBoosterLocations(BoosterCode.C).size() + state.getNumAvailableBooster(BoosterCode.C);
            extensionsPerBot = (int) Math.ceil(numExtensions / numRobots);
        }

        while (!state.mapFinished()) {
            state.coolBoosterDown();
            for (Robot robot : new ArrayList<>(robots)) {
                if (!robot.knowsWhatToDo()) {
                    //the robot needs advise - figure out what to do
                    discoverAction(robot);
                }
                //if the robot knows what to do, let him (if he doesnt, we are actually done
                if (robot.knowsWhatToDo()) performAction(robot);
            }
        }
        return combineResults();
    }
    private boolean canDropTeleport(Robot robot){
        return middle.manhattanDistance(robot.position) <= middle.getX()/2 && state.getTeleportTargets().stream().allMatch(t -> t.manhattanDistance(robot.position) >= 50);
    }
    private void discoverAction(Robot robot) {        
        //install a teleport if available
        if (state.boosterAvailable(BoosterCode.R) && canDropTeleport(robot) && USE_TELEPORT && !state.getBoosterLocations(BoosterCode.X).contains(robot.position) 
        && !state.getTeleportTargets().contains(robot.position)) {
            scheduleAction(R, robot);
            return;
        }

        //attach manipulator if we have less than the maximum (discount four for the body and starting manipulators)
        if (state.boosterAvailable(BoosterCode.B) && robot.getManipulators().size() - 4 < extensionsPerBot) {
            scheduleAction(B, robot);
            return;
        }

        //collect clone
        if (state.mapHasBooster(BoosterCode.C)) {
            collectBooster(robot, BoosterCode.C);
            return;
        }

        //use clone booster
        if (state.boosterAvailable(BoosterCode.C) && state.getBoosterLocations(BoosterCode.X).contains(robot.position)) {
            scheduleAction(C, robot);
            return;
        }

        //got to mystery platform
        if (state.boosterAvailable(BoosterCode.C) && state.mapHasBooster(BoosterCode.X)) {
            collectBooster(robot, BoosterCode.X);
            return;
        }

        if (robot.isBoosterCollector() && state.mapHasBooster(BoosterCode.R)) {
            collectBooster(robot, BoosterCode.R);
            return;
        }

        //collect manipulator
        if (robot.isBoosterCollector() && state.mapHasBooster(BoosterCode.B)) {
            collectBooster(robot, BoosterCode.B);
            return;            
        }
        

        //fill that connected area
        //if all adjacent fields are filled, break
        if (hasFreeNeighbours(robot.position)) {
            //if there is no wall/wrap to my right, turn right and move forward
            Point right = getMyRight(robot);
            if (state.needsWrapping(right)) {
                if (state.needsWrapping(getDoubleRight(robot))) {
                    scheduleAction(E, robot);
                }
                move(robot, robot.position, right);
                return;
            }
            //if there is a need for wrap ahead, move on
            Point front = getMyFront(robot);
            if (state.needsWrapping(front)) {
                move(robot, robot.position, front);
                return;
            }
            //otherwise, turn left
            scheduleAction(Q, robot);
            return;
        }
        //find an empty field
        setupStartLocation(robot);
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

    private void collectBooster(Robot robot, BoosterCode booster) {
        List<Point> manipulatorBoosters = state.getBoosterLocations(booster);
        if (!manipulatorBoosters.isEmpty()) {
            Point next = getNearestPoint(manipulatorBoosters, robot.position);
            moveUntilThere(robot, next);
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

    private boolean hasFreeNeighbours(Point point) {
        return state.needsWrapping(point.up()) ||
<<<<<<< HEAD
               state.needsWrapping(point.down()) ||
               state.needsWrapping(point.left()) ||
               state.needsWrapping(point.right());
=======
                state.needsWrapping(point.down()) ||
                state.needsWrapping(point.left()) ||
                state.needsWrapping(point.right());
>>>>>>> ccd64b4730ee928174c0a6d384f2aca49149a658
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
        Point best = state.getNearestUnwrapped(robot.position);        
        if (best == null) {
            //we are done, yay!
            return;
        }
        //if a robot picks the target of another robot... get furthest point available.
        Point search = best;
        if(robots.size() > 1 && robots.stream().anyMatch(r -> !r.equals(robot) && r.getTarget() != null && r.getTarget().equals(search))){
            best = state.getFurthestUnwrapped(robot.position, best);            
        }
        robot.setTarget(best);
        Collection<Point> path = finder.getPath(robot.position, best);
        moveUntilFree(robot, path);
    }

    private void moveUntilFree(Robot robot, Collection<Point> path) {
        int maxLength = 20 + cutPathCounter;
        int steps = 0;
        Point from = robot.position;
        for (Point p : path) {
            from = move(robot, from, p);
            steps++;
            //as soon as we find an area to fill, we go for it
            if (hasFreeNeighbours(from)) {
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
        Collection<Point> path = finder.getPath(robot.position, target);
        Point from = robot.position;
        for (Point p : path) {
            from = move(robot, from, p);
        }
    }

    private Point move(Robot robot, Point from, Point target) {
        Direction direction = Direction.of(from, target);
        if (direction == null) {
            //is it a teleport?
            if (state.isTeleportTarget(target)) {
                scheduleAction(new Action(ActionType.T, target), robot);
                return target;
            }
            throw new RuntimeException("Invalid move: " + from + " to " + target);
        }
        switch (direction) {
            case EAST:  scheduleAction(D, robot); break;
            case WEST:  scheduleAction(A, robot); break;
            case NORTH: scheduleAction(W, robot); break;
            case SOUTH: scheduleAction(S, robot); break;
            default: throw new RuntimeException("Invalid direction: " + direction.name());
        }
        return target;
    }

    private void scheduleAction(Action action, Robot robot) {
        robot.tellWhatToDo(action);
    }

    private void performAction(Robot robot) {
        Action action = robot.getWhatToDo();
        switch (action.type) {
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
            case C: state.removeBooster(BoosterCode.C);
                    Robot newBot = robot.cloneBot();
                    robots.add(newBot);
                    markFieldsWrapped(newBot);
                    break;
            case R: state.removeBooster(BoosterCode.R);
                    robot.addTeleporter();
                    finder.addTeleport(robot.position);
                    state.addTeleportTarget(robot.position);
                    break;
            case T: robot.teleport(action);
                    markFieldsWrapped(robot);
                    break;
            default: throw new RuntimeException("Action not implemented: " + action.toString());
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

    private List<ActionSequence> combineResults() {
        return robots.stream().map(Robot::getActionLog).collect(Collectors.toList());
    }
}
