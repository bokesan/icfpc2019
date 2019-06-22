package icfpc2019.pathfinder;

import java.util.*;

import icfpc2019.*;

public class Pathfinder{

    private final OpenList openList = new OpenList();
    private HashSet<StarNode> closedList;
    private LinkedList<Point> teleporters = new LinkedList<>();
    private StarNode[][] nodes;
    private int width;
    private int height;

    public void initNodes(Grid grid){
        width = grid.getFields().length -1;
        height = grid.getFields()[0].length -1;
        nodes = new StarNode[width+1][height+1];

        for(int y = 0; y < grid.getFields()[0].length; y++){
            for(int x = 0; x < grid.getFields().length; x++){
                StarNode temp = new StarNode(x, y);
                temp.setIsWalkable(grid.isFree(Point.of(x, y)));
                nodes[x][y] = temp;
            }
        }
    }

    public final List<StarNode> findPath(Point start, Point end, int penalty) {
        openList.clear();
        closedList = new HashSet<>();
        openList.add(nodes[start.getX()][start.getY()]);
        for (;;) {
            StarNode current = openList.poll();
            if (current == null) {
                return Collections.emptyList();
            }
            closedList.add(current);
            if ((current.getXPosition() == end.getX()) && (current.getYPosition() == end.getY())) {
                return calcPath(nodes[start.getX()][start.getY()], current);
            }
            List<StarNode> adjacentNodes = getAdjacent(current);
            for (StarNode currentAdj : adjacentNodes) {
                if (!openList.contains(currentAdj)) {
                    currentAdj.setPrevious(current);
                    currentAdj.sethCosts(nodes[start.getX()][start.getY()], nodes[end.getX()][end.getY()]);
                    if (isDirectionChange(current, currentAdj) && !currentAdj.isTeleport()) {
                        currentAdj.setMovementPanelty(penalty);
                    }
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
        ArrayList<StarNode> adj = new ArrayList<StarNode>();
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

    private boolean isDirectionChange(StarNode current, StarNode next){
        if (current.getPrevious() == null)
            return false;
        return current.getXPosition() == current.getPrevious().getXPosition() && current.getPrevious().getXPosition() == next.getXPosition() || current.getYPosition() == current.getPrevious().getYPosition() && current.getPrevious().getYPosition() == next.getYPosition();
    }

    private List<StarNode> calcPath(StarNode start, StarNode goal) {
        LinkedList<StarNode> path = new LinkedList<>();
        for (StarNode curr = goal; !curr.equals(start); curr = curr.getPrevious()) {
            path.addFirst(curr);
        }
        return path;
    }

    public void addTeleport(Point teleport){
        teleporters.add(teleport);
    }
}
