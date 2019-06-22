package icfpc2019.pathfinder;

import java.util.Comparator;
import java.util.PriorityQueue;

public class OpenList {

    private static class NodeComparator implements Comparator<StarNode> {

        @Override
        public int compare(StarNode a, StarNode b) {
            int r = Integer.compare(a.getfCosts(), b.getfCosts());
            if (r != 0) return r;
            r = Integer.compare(a.getXPosition(), b.getXPosition());
            if (r != 0) return r;
            return Integer.compare(a.getYPosition(), b.getYPosition());
        }
    }

    private final PriorityQueue<StarNode> q = new PriorityQueue<>(100, new NodeComparator());

    /**
     * Get and remove least element.
     */
    public StarNode poll() {
        return q.poll();
    }

    public boolean contains(StarNode node) {
        return q.contains(node);
    }

    public void add(StarNode node) {
        q.add(node);
    }

    public void clear() {
        q.clear();
    }
}
