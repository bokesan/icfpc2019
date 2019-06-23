package icfpc2019;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Robot {

    Point position;
    Direction direction;
    private List<Point> manipulators;
    private StringBuilder log = new StringBuilder();
    private int fastWheelUnits = 0;
    private int drillUnits = 0;

    public Robot(Point position) {
        this.position = position;
        manipulators = new ArrayList<>();
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

    String getActionLog() {
        return log.toString();
    }

    public List<Point> getManipulators() {
        return new ArrayList<>(manipulators);
    }

    void singleStep(Action action) {
        switch (action) {
            case W: position = position.up();       moveManipulators(0, 1);     break;
            case A: position = position.left();     moveManipulators(-1, 0);    break;
            case S: position = position.down();     moveManipulators(0, -1);    break;
            case D: position = position.right();    moveManipulators(1, 0);     break;
        }
        log.append(action.toString());
        countTimeUnit();
    }

    public void turn(Action action) {
        switch (action) {
            case Q: turnLeft(); break;
            case E: turnLeft(); turnLeft(); turnLeft(); break;
            default: throw new RuntimeException("Invalid turning direction: " + action.name());
        }
        log.append(action.toString());
        countTimeUnit();
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

