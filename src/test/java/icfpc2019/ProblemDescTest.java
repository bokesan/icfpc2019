package icfpc2019;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProblemDescTest {

    @Test
    public void parseSimple() {
        String problem = "(1,2),(2,3)#(5,6)##";
        ProblemDesc desc = ProblemDesc.of(problem);
        assertEquals(2, desc.getMap().size());
        assertEquals(Point.of(1,2), desc.getMap().get(0));
        assertEquals(Point.of(2,3), desc.getMap().get(1));
        assertEquals(Point.of(5,6), desc.getInitialWorkerLocation());
        assertTrue(desc.getObstacles().isEmpty());
        assertTrue(desc.getBoosters().isEmpty());
    }

    @Test
    public void parseAllComponents() {
        String problem = "(1,2),(2,3)#(5,6)#(4,4),(5,5);(9,9)#B(10,10);L(11,11)";
        ProblemDesc desc = ProblemDesc.of(problem);
        assertEquals(2, desc.getMap().size());
        assertEquals(Point.of(1,2), desc.getMap().get(0));
        assertEquals(Point.of(2,3), desc.getMap().get(1));
        assertEquals(Point.of(5,6), desc.getInitialWorkerLocation());
        assertEquals(2, desc.getObstacles().size());
        assertEquals(2, desc.getObstacles().get(0).size());
        assertEquals(1, desc.getObstacles().get(1).size());
        assertEquals(2, desc.getBoosters().size());
        assertEquals(new BoosterLocation(BoosterCode.L, Point.of(11,11)), desc.getBoosters().get(1));
    }

}
