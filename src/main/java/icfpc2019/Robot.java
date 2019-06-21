package icfpc2019;

import java.util.LinkedList;
import java.util.List;


public class Robot {
    private enum Direction{
        NORTH,
        EAST,
        WEST,
        SOUTH    
    }

    Point position;
    Direction direction;
    private List<Point> manipulators;
    private StringBuilder log = new StringBuilder();
     
    public Robot(Point position) {
        this.position = position;
        manipulators = new LinkedList<>();
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

    public void move(Point newPosition){
        if((newPosition.getX() < position.getX()) && position.getY() == newPosition.getY())
              log(Actions.A);
        else if((newPosition.getX() > position.getX()) && position.getY() == newPosition.getY())
              log(Actions.D);
        else if((newPosition.getY() > position.getY()) && position.getX() == newPosition.getX())
              log(Actions.W);
        else if((newPosition.getY() < position.getY()) && position.getX() == newPosition.getX())
              log(Actions.S);
        position = newPosition;
    }

    private void log(Actions action) {
        log.append(action.toString());
    }

    public void spin(Actions action) {
        switch(action){
            case E:
                turnRight();            
                log(action);
                break;
            case Q:
                turnLeft();
                log(action);                 
                break;
            default:
                break;
        }
    }

    private void turnLeft(){
        Direction manipulationWay = null;
        List<Point> tempList = new LinkedList<>();
        for (Point point : manipulators) {
            int xDifference = point.getX() - position.getX();
            int yDifference = point.getY() - position.getY(); 
            switch(direction){
                case NORTH:
                    tempList.add(Point.of(position.getX() - yDifference, position.getY() - xDifference));
                    manipulationWay = Direction.WEST;
                    break;
                case EAST:
                    tempList.add(Point.of(position.getX() + yDifference, position.getY() + xDifference));
                    manipulationWay = Direction.NORTH;
                    break;
                case WEST:
                    tempList.add(Point.of(position.getX() + yDifference, position.getY() + xDifference));
                    manipulationWay = Direction.SOUTH;
                    break;
                case SOUTH:
                    tempList.add(Point.of(position.getX() -yDifference, position.getY() + xDifference));
                    manipulationWay = Direction.EAST;
                    break;
            }            
        }   
        if(manipulationWay != null){
            direction = manipulationWay;         
        }
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

