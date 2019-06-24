package icfpc2019.pathfinder;

import icfpc2019.Grid;
import icfpc2019.Point;

import java.util.ArrayDeque;
import java.util.Arrays;

public class DistanceMap {

    private static final class QueueEntry {
        final Point point;
        final int dist;
        QueueEntry(Point p, int d) {
            this.point = p;
            this.dist = d;
        }
    }

    private final Grid grid;

    private final int width;
    private final int height;

    private final int[][] distance;

    public DistanceMap(Grid grid) {
        this.grid = grid;
        this.width = grid.getWidth();
        this.height = grid.getHeight();
        this.distance = new int[width][height];
    }

    public void from(Point point) {
        clear();
        ArrayDeque<QueueEntry> queue = new ArrayDeque<>();
        queue.push(new QueueEntry(point, 0));
        set(point, 0);
        for (;;) {
            QueueEntry p = queue.poll();
            if (p == null)
                break;
            if (grid.isFree(p.point)) {
                check(queue, p.point.up(), p.dist + 1);
                check(queue, p.point.down(), p.dist + 1);
                check(queue, p.point.left(), p.dist + 1);
                check(queue, p.point.right(), p.dist + 1);
            }
        }
    }

    private void check(ArrayDeque<QueueEntry> queue, Point p, int dist) {
        if (!grid.isFree(p)) {
            return;
        }
        int d = get(p);
        if (d >= 0) {
            return;
        }
        queue.addLast(new QueueEntry(p, dist));
        set(p, dist);
    }


    public int get(int x, int y) {
        return distance[x][y];
    }

    public int get(Point location) {
        return get(location.getX(), location.getY());
    }

    private void clear() {
        for (int[] col : distance) {
            Arrays.fill(col, -1);
        }
    }

    private void set(Point p, int value) {
        distance[p.getX()][p.getY()] = value;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                b.append(' ').append(get(x,y));
            }
            b.append('\n');
        }
        return b.toString();
    }

}
