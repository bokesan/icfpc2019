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
        extendManipulators(Point.of(position.getX()+1, position.getY()));
        extendManipulators(Point.of(position.getX()+1, position.getY()+1));
        extendManipulators(Point.of(position.getX()+1, position.getY()-1));
    }

    private void extendManipulators(Point manipulatorExtension){
        if(manipulators.contains(manipulatorExtension)) {
            throw new ExtensionException("Already extended");
        }
        manipulators.add(manipulatorExtension);
    }    

    public void move(StarNode node){
        int dx = node.getXPosition() - position.getX();
        int dy = node.getYPosition() - position.getY();
        Actions action;
        if (dy == 0 && !node.isTeleport()) {
            switch (dx) {
                case -1:  action = Actions.A; break;
                case 1:   action = Actions.D; break;                
                default: throw new InvalidMoveException(position, node.getAsPoint());
            }
        } else if (dx == 0 && !node.isTeleport()) {
            switch (dy) {
                case -1:  action = Actions.S; break;
                case 1:   action = Actions.W; break;
                default: throw new InvalidMoveException(position, node.getAsPoint());
            }
        } else if(node.isTeleport()) {
            action = Actions.T;
        } else {
            throw new InvalidMoveException(position, node.getAsPoint());
        }
        if(action == Actions.T) {
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

    private void log(Actions action) {
        log.append(action.toString());
    }

    private void log(Actions action, Point target){
        log.append(action.toString()).append("(").append(target.getX()).append(",").append(target.getY()).append(")");
    }

    public void spin(Actions action) {
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

    public void addBooster(BoosterCode boosterCode){
        gatheredBosters.add(boosterCode);
    }

    public List<BoosterCode> getGatheredBoosters(){
        return gatheredBosters;
    }

    public boolean useBooster(BoosterCode boosterCode){
        countTimeUnit();
        if(gatheredBosters.contains(boosterCode)){
            gatheredBosters.remove(boosterCode);
            return boost(boosterCode);
        }
        return false;
    }

    private boolean boost(BoosterCode boosterCode) {
        switch(boosterCode){
            case B:
                // attach to side of existing manipulators
                Direction origDir = direction;
                while (direction != Direction.EAST)
                    turnLeft();
                Point p;
                for (int offs = 2; ; offs++) {
                    p = position.translate(1, offs);
                    if (!manipulators.contains(p))
                        break;
                    p = position.translate(1, -offs);
                    if (!manipulators.contains(p))
                        break;
                }
                manipulators.add(p);
                while (direction != origDir)
                    turnLeft();
                log(Actions.B, Point.of(p.getX() - position.getX(), p.getY() - position.getY()));
                break;
            case F:
                fastWheelUnits = 50;
                break;
            case L:
                drillUnits = 30;
                break;
            case R:                
                log(Actions.R);
                break;
            case C:
                log(Actions.C);
                break;
        }
        return true;
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

    public String getActionLog() {
        return log.toString();
    }

    public List<Point> getManipulators() {
        return manipulators;
    }

    public static class ExtensionException extends RuntimeException {
        public ExtensionException(String msg){
            super(msg);
        }
    }

    public static class InvalidMoveException extends RuntimeException {
        public InvalidMoveException(Point position, Point newPosition) {
            super("invalid move attempted from " + position + " to " + newPosition);
        }
    }
}

