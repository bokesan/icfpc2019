package icfpc2019;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RobotTest {

    @Test
    public void testNew() {
        Point p = Point.of(10, 20);
        Robot r = new Robot(p);
        // a new robot should face east
        assertEquals(p, r.position);
        Collection<Point> man = r.getManipulators();
        assertEquals(3, man.size());
        assertTrue(man.contains(p.right()));
        assertTrue(man.contains(p.right().up()));
        assertTrue(man.contains(p.right().down()));
    }

    @Test
    public void testMoveRight() {
        Point p = Point.of(10, 20);
        Point p1 = p.right();
        Robot r = new Robot(p);
        r.move(p1);
        // a new robot should face east
        assertEquals(p1, r.position);
        Collection<Point> man = r.getManipulators();
        assertEquals(3, man.size());
        assertTrue(man.contains(p1.right()));
        assertTrue(man.contains(p1.right().up()));
        assertTrue(man.contains(p1.right().down()));
    }

}
