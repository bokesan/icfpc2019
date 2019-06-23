package icfpc2019;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Robot {

    Point position;
    Direction direction;
    private List<Point> manipulators;
    private final ActionSequence log = new ActionSequence();
    private int fastWheelUnits = 0;
    private int drillUnits = 0;
    private List<Action> toDoList = new ArrayList<>();

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
        if(drillUnits > 0) drillUnits--;
        if(fastWheelUnits > 0) fastWheelUnits--;
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

    ActionSequence getActionLog() {
        return log;
    }

    public List<Point> getManipulators() {
        return new ArrayList<>(manipulators);
    }

    void singleStep(Action action) {
        switch (action.type) {
            case W: position = position.up();       moveManipulators(0, 1);     break;
            case A: position = position.left();     moveManipulators(-1, 0);    break;
            case S: position = position.down();     moveManipulators(0, -1);    break;
            case D: position = position.right();    moveManipulators(1, 0);     break;
        }
        log.append(action);
        countTimeUnit();
    }

    void turn(Action action) {
        switch (action.type) {
            case Q: turnLeft(); break;
            case E: turnLeft(); turnLeft(); turnLeft(); break;
            default: throw new RuntimeException("Invalid turning direction: " + action.toString());
        }
        log.append(action);
        countTimeUnit();
    }

    void attachManipulator(Point p) {
        //todo check validity etc
        manipulators.add(p);
        log.append(ActionType.B, p.translate(Point.of(-position.getX(), -position.getY())));
        countTimeUnit();
    }

    boolean knowsWhatToDo() {
        return !toDoList.isEmpty();
    }

    Action getWhatToDo() {
        return toDoList.remove(0);
    }

    void tellWhatToDo(Action action) {
        toDoList.add(action);
    }

    Robot cloneBot() {
        Robot newBot = new Robot(position);
        log.append(Action.C);
        countTimeUnit();
        return newBot;
    }

    void addTeleporter() {
        log.append(Action.R);
        countTimeUnit();
    }

    public void teleport(Action action) {
        int divX = action.point.getX() - position.getX();
        int divy = action.point.getY() - position.getY();
        position = action.point;
        moveManipulators(divX, divy);
        log.append(action);
        countTimeUnit();
    }

    public static class ExtensionException extends RuntimeException {
        ExtensionException(String msg){
            super(msg);
        }
    }
}
