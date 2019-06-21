package icfpc2019;

import java.util.LinkedList;
import java.util.List;

public class Robot {

    Point position;
    private List<Point> manipulators;
    private StringBuilder log = new StringBuilder();

    public Robot(Point position) {
        this.position = position;
        manipulators = new LinkedList<>();
        initBaseManipulators();
    }
    private void initBaseManipulators(){
        extendManipulators(Point.of(position.getX()+1, position.getY()));
        extendManipulators(Point.of(position.getX()+1, position.getY()+1));
        extendManipulators(Point.of(position.getX(), position.getY()+1));
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
                log(action);
                alignManipulators();
                break;
            case Q:
                log(action); 
                alignManipulators();
                break;
            default:
                break;
        }
    }

    private void alignManipulators(){
        for (Point point : manipulators) {
            System.out.println("old --- X:"+ point.getX() +" Y:" + point.getY());

            int xDifference = point.getX() - position.getX();
            int yDifference = point.getY() - position.getY();


            System.out.println("xDif:"+xDifference);
            System.out.println("yDif:"+yDifference);
            point = Point.of(point.getX()-xDifference, point.getY()-yDifference);
            System.out.println("new --- X:"+ point.getX() +" Y:" + point.getY());
        }   
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

