package icfpc2019;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import icfpc2019.pathfinder.StarNode;

public class Robot {

    Point position;
    Direction direction;
    private List<Point> manipulators;
    private List<BoosterCode> gatheredBosters;
    private StringBuilder log = new StringBuilder();
    private int fastWheelUnits = 0;
    private int drillUnits = 0;

    public Robot(Point position) {
        this.position = position;
        manipulators = new ArrayList<>();
        gatheredBosters = new LinkedList<>();
        initBaseManipulators();
        direction = Direction.EAST;
    }

    private void initBaseManipulators(){
        addManipulator(Point.of(position.getX(), position.getY()));
        addManipulator(Point.of(position.getX()+1, position.getY()));
        addManipulator(Point.of(position.getX()+1, position.getY()+1));
        addManipulator(Point.of(position.getX()+1, position.getY()-1));
    }

    private void addManipulator(Point manipulatorExtension){
        if(manipulators.contains(manipulatorExtension)) {
            throw new ExtensionException("Already extended");
        }
        manipulators.add(manipulatorExtension);
    }

    public void move(StarNode node){
        int dx = node.getXPosition() - position.getX();
        int dy = node.getYPosition() - position.getY();
        Action action;
        if (dy == 0 && !node.isTeleport()) {
            switch (dx) {
                case -1:  action = Action.A; break;
                case 1:   action = Action.D; break;
                default: throw new InvalidMoveException(position, node.getAsPoint());
            }
        } else if (dx == 0 && !node.isTeleport()) {
            switch (dy) {
                case -1:  action = Action.S; break;
                case 1:   action = Action.W; break;
                default: throw new InvalidMoveException(position, node.getAsPoint());
            }
        } else if(node.isTeleport()) {
            action = Action.T;
        } else {
            throw new InvalidMoveException(position, node.getAsPoint());
        }
        if(action == Action.T) {
            log(action, node.getAsPoint());
        } else {
            log(action);
        }
        moveManipulators(dx, dy);
        countTimeUnit();
        position = node.getAsPoint();
    }

    private void moveManipulators(int x, int y) {
        int n = manipulators.size();
        for (int i = 0; i < n; i++) {
            manipulators.set(i, manipulators.get(i).translate(x, y));
        }
    }

    private void countTimeUnit(){
        if(drillUnits > 0)
            drillUnits -= 1;
        if(fastWheelUnits > 0)
            fastWheelUnits -= 1;
    }

    void spin(Action action) {
        switch (action) {
            case E:
                turnRight();
                log(action);
                countTimeUnit();
                break;
            case Q:
                turnLeft();
                log(action);
                countTimeUnit();
                break;
            default:
                throw new AssertionError();
        }
    }

    void addBooster(BoosterCode boosterCode){
        gatheredBosters.add(boosterCode);
    }

    List<BoosterCode> getGatheredBoosters(){
        return gatheredBosters;
    }

    void useBooster(BoosterCode boosterCode){
        countTimeUnit();
        if(gatheredBosters.contains(boosterCode)){
            gatheredBosters.remove(boosterCode);
            boost(boosterCode);
        }
    }

    private void boost(BoosterCode boosterCode) {
        switch(boosterCode){
            case B:
                // attach to side of existing manipulators
                Point p = attachManipulator();
                manipulators.add(p);
                log(Action.B, Point.of(p.getX() - position.getX(), p.getY() - position.getY()));
                break;
            case F:
                fastWheelUnits = 50;
                break;
            case L:
                drillUnits = 30;
                break;
            case R:
                log(Action.R);
                break;
            case C:
                log(Action.C);
                break;
        }
    }

    private Point attachManipulator() {
        //todo move to solver, change strategy
        Point p;
        switch (direction) {
            case EAST:
                for (int offs = 2; ; offs++) {
                    p = position.translate(1, offs);
                    if (!manipulators.contains(p))
                        return p;
                    p = position.translate(1, -offs);
                    if (!manipulators.contains(p))
                        return p;
                }
            case WEST:
                for (int offs = 2; ; offs++) {
                    p = position.translate(-1, offs);
                    if (!manipulators.contains(p))
                        return p;
                    p = position.translate(-1, -offs);
                    if (!manipulators.contains(p))
                        return p;
                }
            case NORTH:
                for (int offs = 2; ; offs++) {
                    p = position.translate(offs, 1);
                    if (!manipulators.contains(p))
                        return p;
                    p = position.translate(-offs, 1);
                    if (!manipulators.contains(p))
                        return p;
                }
            case SOUTH:
                for (int offs = 2; ; offs++) {
                    p = position.translate(offs, -1);
                    if (!manipulators.contains(p))
                        return p;
                    p = position.translate(-offs, -1);
                    if (!manipulators.contains(p))
                        return p;
                }
            default:
                throw new AssertionError();
        }
    }


    private void turnLeft(){
        Direction manipulationWay = null;
        List<Point> tempList = new LinkedList<>();
        for (Point point : manipulators) {
            int xDifference = point.getX() - position.getX();
            int yDifference = point.getY() - position.getY();
            tempList.add(Point.of(position.getX() - yDifference, position.getY() + xDifference));
        }
        switch(direction){
            case NORTH:
                manipulationWay = Direction.WEST;
                break;
            case EAST:
                manipulationWay = Direction.NORTH;
                break;
            case WEST:
                manipulationWay = Direction.SOUTH;
                break;
            case SOUTH:
                manipulationWay = Direction.EAST;
                break;
        }
        direction = manipulationWay;
        manipulators = tempList;
    }

    private void turnRight(){
        turnLeft();
        turnLeft();
        turnLeft();
    }

    String getActionLog() {
        return log.toString();
    }

    public List<Point> getManipulators() {
        return new ArrayList<>(manipulators);
    }

    public void singleStep(Action action) {
        switch (action) {
            case W: position = position.up();    break;
            case A: position = position.left();  break;
            case S: position = position.down();  break;
            case D: position = position.right(); break;
        }
        log.append(action.toString());
    }

    public static class ExtensionException extends RuntimeException {
        ExtensionException(String msg){
            super(msg);
        }
    }

    public static class InvalidMoveException extends RuntimeException {
        InvalidMoveException(Point position, Point newPosition) {
            super("invalid move attempted from " + position + " to " + newPosition);
        }
    }
}

