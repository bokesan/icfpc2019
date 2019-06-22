package icfpc2019;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GridTest {

    @Test
    public void testVisibleNoObstacles() {
        Grid grid = Grid.of(30, 30);

        Point p1 = Point.of(10, 20);
        Point p2 = p1.translate(3, 1);
        assertTrue(grid.visible(p1, p2));
        assertTrue(grid.visible(p2, p1));

        p2 = p1.translate(3, -1);
        assertTrue(grid.visible(p1, p2));
        assertTrue(grid.visible(p2, p1));

        p2 = p1.translate(-3, 1);
        assertTrue(grid.visible(p1, p2));
        assertTrue(grid.visible(p2, p1));

        p2 = p1.translate(1, -3);
        assertTrue(grid.visible(p1, p2));
        assertTrue(grid.visible(p2, p1));
    }

    @Test
    public void testVisible21() {
        Grid grid = Grid.of(30, 30,Point.of(11, 20));
        Point p1 = Point.of(10, 20);
        Point p2 = p1.translate(2, 1);
        assertFalse(grid.visible(p1, p2));
        assertFalse(grid.visible(p2, p1));
    }

    @Test
    public void testVisible2_1() {
        Grid grid = Grid.of(30, 30,Point.of(11, 20));
        Point p1 = Point.of(10, 20);
        Point p2 = p1.translate(2, -1);
        assertFalse(grid.visible(p1, p2));
        assertFalse(grid.visible(p2, p1));
    }

    @Test
    public void testVisible3_1() {
        Grid grid = Grid.of(30, 30,Point.of(11, 20));
        Point p1 = Point.of(10, 20);
        Point p2 = p1.translate(3, -1);
        assertFalse(grid.visible(p1, p2));
        assertFalse(grid.visible(p2, p1));
    }

    @Test
    public void testVisible12() {
        Grid grid = Grid.of(30, 30,Point.of(10, 21));
        Point p1 = Point.of(10, 20);
        Point p2 = p1.translate(1, 2);
        assertFalse(grid.visible(p1, p2));
        assertFalse(grid.visible(p2, p1));
    }

    @Test
    public void testVisible_12() {
        Grid grid = Grid.of(30, 30, Point.of(10, 21));
        Point p1 = Point.of(10, 20);
        Point p2 = p1.translate(-1, 2);
        assertFalse(grid.visible(p1, p2));
        assertFalse(grid.visible(p2, p1));
    }

    @Test
    public void testGetFreeSquares() {
        Grid grid = Grid.of(10, 10, Point.origin(), Point.of(4,5));
        List<Point> free = grid.getFreeSquares();
        assertEquals(98, free.size());
    }
}
