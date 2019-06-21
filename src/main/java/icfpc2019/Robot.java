package icfpc2019;

import java.util.LinkedList;
import java.util.List;

enum Actions{
    W, //move up
    A, //move left
    S, //move down
    D, //move right
    Z, //do nothing
    E, //turn 90° clockwise
    Q, //turn 90° anti-clockwise
    F, //attach fast wheel
    L, //use drill
    B // extend new arm
}
enum Direction{
    NORTH,
    EAST,
    WEST,
    SOUTH    
}
public class Robot{
    Point position;
    Direction direction;
    List<Point> Manipulators;
    public Robot(Point position) {
        this.position = position;
        Manipulators = new LinkedList<>();        
        direction = Direction.EAST;        
        initBaseManipulators();
    }
    private void initBaseManipulators(){
        extendManipulators(Point.of(position.getX()+1, position.getY()));
        extendManipulators(Point.of(position.getX()+1, position.getY()+1));
        extendManipulators(Point.of(position.getX()+1, position.getY()-1));
    }

    public void extendManipulators(Point manipulatorExtension){
        if(Manipulators.contains(manipulatorExtension))
                throw new ExtensionException("Already extended");
        Manipulators.add(manipulatorExtension);
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
    private void log(Actions action){

    }

    public void spin(Actions action) {
        switch(action){
            case E:
                log(action);
                alignManipulators();
                break;
            case Q:
                log(action); 
                alignManipulators();
                alignManipulators();
                alignManipulators();
                alignManipulators();
                alignManipulators();
                break;
            default:
                break;
        }
    }
    private void alignManipulators(){
        Direction manipulationWay = null;
        List<Point> tempList = new LinkedList<>();
        for (Point point : Manipulators) {
            
            System.out.println("old --- X:"+ point.getX() +" Y:" + point.getY());

            int xDifference = point.getX() - position.getX();
            int yDifference = point.getY() - position.getY();            
            int resX = 0;            
            int resY = 0;   

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
        for (Point p : tempList) {
            System.out.println("new--- X:"+ p.getX() +" Y:" + p.getY());
        }
        Manipulators = tempList;
        System.out.println("DONE: " + direction);
    }

    public static class ExtensionException extends RuntimeException {
        public ExtensionException(String msg){
            super(msg);
        }
    }
}

