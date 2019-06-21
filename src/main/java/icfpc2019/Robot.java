package icfpc2019;

import java.awt.Desktop.Action;
import java.security.cert.Extension;
import java.util.ArrayList;
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
public class Robot{
    Point position;
    List<Point> Manipulators;
    public Robot(Point position) {
        Manipulators = new ArrayList<Point>();        
        initBaseManipulators();
    }
    private void initBaseManipulators(){
        extendManipulators(Point.of(position.getX()+1, position.getY()));
        extendManipulators(Point.of(position.getX()+1, position.getY()+1));
        extendManipulators(Point.of(position.getX(), position.getY()+1));
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
        else if((newPosition.getY() > position.getY()) && position.getX() == newPosition.getX())
              log(Actions.S);
        position = newPosition;
    }
    private void log(Actions action){

    }

    public void spin(Actions action) {
        switch(action){
            case E:
                log(action);
                break;
            case Q:
                log(action); 
                break;
            default:
                break;
        }
    }
    private void alignManipulators(){
        
    }

    public static class ExtensionException extends RuntimeException {
        public ExtensionException(String msg){
            super(msg);
        }
    }
}

