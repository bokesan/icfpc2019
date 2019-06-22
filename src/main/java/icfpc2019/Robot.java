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
        manipulators = new LinkedList<>();
        gatheredBosters = new LinkedList<>();
        initBaseManipulators();
        direction = Direction.EAST;  
    }
    private void initBaseManipulators(){
        extendManipulators(Point.of(position.getX()+1, position.getY()));
        extendManipulators(Point.of(position.getX()+1, position.getY()+1));
        extendManipulators(Point.of(position.getX()+1, position.getY()-1));
    }

    public void extendManipulators(Point manipulatorExtension){
        if(manipulators.contains(manipulatorExtension))
                throw new ExtensionException("Already extended");
        manipulators.add(manipulatorExtension);
    }    

    public void move(StarNode node){
        move(node, true);
    }
    public void move(StarNode node, boolean loggingActive){       

        if(loggingActive){
            
            if((node.getXPosition() < position.getX()) && position.getY() == node.getYPosition()) {
                log(Actions.A);
                moveManipulators(-1, 0);
            } else if((node.getXPosition() > position.getX()) && position.getY() ==  node.getYPosition()) {
                log(Actions.D);
                moveManipulators(1, 0);
            } else if((node.getYPosition() > position.getY()) && position.getX() == node.getXPosition()) {
                log(Actions.W);
                moveManipulators(0, 1);
            } else if(( node.getYPosition() < position.getY()) && position.getX() == node.getXPosition()) {
                log(Actions.S);
                moveManipulators(0, -1);
            }else if(node.isTeleport()){
                log(Actions.T, Point.of(node.getXPosition(), node.getYPosition()));                
            }            
            countTimeUnit();
        }
        position = Point.of(node.getXPosition(), node.getYPosition());
    }

    private void moveManipulators(int x, int y) {
        List<Point> newManipulators = new ArrayList<>();
        for (Point p : manipulators) {
            newManipulators.add(Point.of(p.getX() + x, p.getY() + y));
        }
        manipulators = newManipulators;
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
        switch(action){
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
                break;
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
            return boost(boosterCode);
        }
        return false;
    }
    private boolean boost(BoosterCode boosterCode){
        switch(boosterCode){
            case B:
            //TODO: implement attachment extension
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
            case X:
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
}

