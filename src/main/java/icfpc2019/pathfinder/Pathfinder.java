package icfpc2019.pathfinder;

import java.util.*;

import icfpc2019.*;

public class Pathfinder{

    private final OpenList openList = new OpenList();
    private final Set<StarNode> closedList = new HashSet<>();
    private final List<Point> teleporters = new ArrayList<>();
    private StarNode[][] nodes;
    private int width;
    private int height;

    public void initNodes(Grid grid){
        width = grid.getFields().length - 1;
        height = grid.getFields()[0].length - 1;
        nodes = new StarNode[width+1][height+1];

        for(int y = 0; y <= height; y++){
            for(int x = 0; x <= width; x++){
                StarNode temp = new StarNode(x, y);                
                temp.setIsWalkable(grid.isFree(Point.of(x, y)));
                nodes[x][y] = temp;
            }
        }
    }

    private StarNode findPath(Point start, Point end) {
        openList.clear();
        closedList.clear();
        openList.add(nodes[start.getX()][start.getY()]);
        for (;;) {
            StarNode current = openList.poll();
            if (current == null) {
                return null;
            }
            closedList.add(current);
            if ((current.getXPosition() == end.getX()) && (current.getYPosition() == end.getY())) {
                return current;
            }
            List<StarNode> adjacentNodes = getAdjacent(current);
            for (StarNode currentAdj : adjacentNodes) {
                if (!openList.contains(currentAdj)) {
                    currentAdj.setPrevious(current);
                    currentAdj.sethCosts(nodes[start.getX()][start.getY()], nodes[end.getX()][end.getY()]);
                    currentAdj.setgCosts(current);
                    openList.add(currentAdj);
                } else {
                    if (currentAdj.getgCosts() > currentAdj.calculategCosts(current)) {
                        currentAdj.setPrevious(current);
                        currentAdj.setgCosts(current);
                    }
                }
            }
        }
    }

    private ArrayList<StarNode> getAdjacent(StarNode current) {
        int x = current.getXPosition();
        int y = current.getYPosition();
        ArrayList<StarNode> adj = new ArrayList<>();
        StarNode temp;
        if (x > 0) {
            temp = nodes[x-1][y];
            tryAddTempToAdjacent(adj, temp);
        }

        if (x < width) {
            temp = nodes[x+1][y];
            tryAddTempToAdjacent(adj, temp);
        }

        if (y > 0) {
            temp = nodes[x][y-1];
            tryAddTempToAdjacent(adj, temp);
        }

        if (y < height) {
            temp = nodes[x][y+1];
            tryAddTempToAdjacent(adj, temp);
        }
        for (Point p : teleporters) {
            StarNode teleporterNode = nodes[p.getX()][p.getY()];
            teleporterNode.setIsTeleport(true);
            tryAddTempToAdjacent(adj, teleporterNode);
        }
        return adj;
    }

    private void tryAddTempToAdjacent(ArrayList<StarNode> adj, StarNode temp){
        if (temp.isWalkable() && !closedList.contains(temp)) {
            temp.setIsDiagonaly(false);
            adj.add(temp);
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
        return calcPathLength(nodes[from.getX()][from.getY()], endNode);
    }

    public Collection<Point> getPath(Point from, Point to) {
        StarNode endNode = findPath(from, to);
        if (endNode == null)
            return Collections.emptyList();
        return calcPath(nodes[from.getX()][from.getY()], endNode);
    }
}
