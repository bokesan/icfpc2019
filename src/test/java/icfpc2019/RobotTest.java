package icfpc2019;

import org.junit.Test;
<<<<<<< HEAD
=======

>>>>>>> d96f58be57deb8fe7887fdefdc897a6cecda000b
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RobotTest {

    @Test
    public void testNew() {
        Point p = Point.of(10, 20);
        Robot r = new Robot(p, true);
        // a new robot should face east
        assertEquals(p, r.position);
        Collection<Point> man = r.getManipulators();
        assertEquals(4, man.size());
        assertTrue(man.contains(p));
        assertTrue(man.contains(p.right()));
        assertTrue(man.contains(p.right().up()));
        assertTrue(man.contains(p.right().down()));
    }

    @Test
    public void testMoveRight() {        
        Point p = Point.of(10, 20);        
        Point p1 = p.right();
        Robot r = new Robot(p,true);
        r.singleStep(Action.D);
        // a new robot should face east
        assertEquals(p1, r.position);
        Collection<Point> man = r.getManipulators();
        assertEquals(4, man.size());
        assertTrue(man.contains(p1));
        assertTrue(man.contains(p1.right()));
        assertTrue(man.contains(p1.right().up()));
        assertTrue(man.contains(p1.right().down()));
    }

}
