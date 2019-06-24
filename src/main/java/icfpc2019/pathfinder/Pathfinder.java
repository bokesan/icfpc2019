package icfpc2019.pathfinder;

import java.util.*;

import icfpc2019.*;

public class Pathfinder{

    private final OpenList openList = new OpenList();
    private final Set<StarNode> closedList = new HashSet<>();
    private final List<Point> teleporters = new ArrayList<>();
    private final StarNode[][] nodes;
    private final int width;
    private final int height;

    public Pathfinder(Grid grid){
        width = grid.getFields().length - 1;
        height = grid.getFields()[0].length - 1;
        nodes = new StarNode[width+1][height+1];

        for(int y = 0; y <= height; y++){
            for(int x = 0; x <= width; x++){
                StarNode temp = new StarNode(x, y);                
                temp.setIsWalkable(grid.isFree(x, y));
                nodes[x][y] = temp;
            }
        }
    }

    private StarNode getNode(Point point) {
        return nodes[point.getX()][point.getY()];
    }

    private StarNode findPath(Point start, Point end) {
        openList.clear();
        closedList.clear();
        StarNode startNode = getNode(start);
        StarNode endNode = getNode(end);
        openList.add(startNode);
        for (;;) {
            StarNode current = openList.poll();
            if (current == null) {
                return null;
            }
            closedList.add(current);
            if ((current.getXPosition() == end.getX()) && (current.getYPosition() == end.getY())) {
                return current;
            }
            markAdjacent(startNode, endNode, current);
        }
    }

    private void markAdjacent(StarNode start, StarNode end, StarNode current) {
        int x = current.getXPosition();
        int y = current.getYPosition();
        if (x > 0) {
            tryAdjacent(start, end, current, nodes[x-1][y]);
        }
        if (x < width) {
            tryAdjacent(start, end, current, nodes[x+1][y]);
        }
        if (y > 0) {
            tryAdjacent(start, end, current, nodes[x][y-1]);
        }
        if (y < height) {
            tryAdjacent(start, end, current, nodes[x][y+1]);
        }
        for (Point p : teleporters) {
            StarNode teleporterNode = nodes[p.getX()][p.getY()];
            teleporterNode.setIsTeleport(true);
            tryAdjacent(start, end, current, teleporterNode);
        }
    }

    private void tryAdjacent(StarNode start, StarNode end, StarNode current, StarNode adj) {
        if (adj.isWalkable() && !closedList.contains(adj)) {
            if (!openList.contains(adj)) {
                adj.setPrevious(current);
                adj.sethCosts(start, end);
                adj.setgCosts(current);
                openList.add(adj);
            } else if (adj.getgCosts() > adj.calculategCosts(current)) {
                adj.setPrevious(current);
                adj.setgCosts(current);
            }
        }
    }

    private Collection<Point> calcPath(StarNode start, StarNode goal) {
        ArrayDeque<Point> path = new ArrayDeque<>();
        for (StarNode curr = goal; !curr.equals(start); curr = curr.getPrevious()) {
            path.addFirst(curr.getAsPoint());
        }
        return path;
    }

    private int calcPathLength(StarNode start, StarNode goal) {
        int n = 0;
        for (StarNode curr = goal; !curr.equals(start); curr = curr.getPrevious()) {
            n++;
        }
        return n;
    }

    public void addTeleport(Point teleport){
        teleporters.add(teleport);
    }

    public int getPathLength(Point from, Point to) {
        StarNode endNode = findPath(from, to);
        if (endNode == null)
            return 0;
        return calcPathLength(getNode(from), endNode);
    }

    public Collection<Point> getPath(Point from, Point to) {
        StarNode endNode = findPath(from, to);
        if (endNode == null)
            return Collections.emptyList();
        return calcPath(getNode(from), endNode);
    }
}
