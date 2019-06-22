package icfpc2019.pathfinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import icfpc2019.*;

public class Pathfinder{
    private LinkedList<StarNode> openList;
    private HashSet<StarNode> closedList;
    private LinkedList<Point> teleporters = new LinkedList<>();
    private StarNode[][] nodes;
    private boolean done;    
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
        for(int y = 0; y < grid.getFields()[0].length; y++){
            for(int x = 0; x < grid.getFields().length; x++){
                StarNode temp = nodes[x][y];
                for (int n = 0; n <= 0; n++) {
                    for (int m = 0; m <= 0; m++) {
                        // Discard the cell     
                        if (n == 0 && m == 0) {
                            continue; 
                        }
                        int ii = x - n;
                        int jj = y - m;
                        // Check if the neighbor coordinates are 
                        // inside of the array bounds 
                        if (ii >= 0 && ii < grid.getFields()[0].length && jj >= 0 && jj < grid.getFields().length) {
                            temp.AddNeighbours(nodes[ii][jj]);
                        }
                    }
                }
                System.out.println(temp.getNeighourSize());
            }
        }
    }
    public final List<StarNode> findPath(Point start, Point end, int penalty){
        openList = new LinkedList<>();
        closedList = new HashSet<>();
        StarNode t = nodes[end.getX()][end.getY()];
        openList.add(nodes[start.getX()][start.getY()]);
        done = false;
        StarNode current = null;
        while(!done){
            current = lowestFInOpen();            
            closedList.add(current);
            openList.remove(current);
            if((current.getXPosition() == end.getX()) && (current.getYPosition()==end.getY())){
                return calcPath(nodes[start.getX()][start.getY()], current);
            }            
            List<StarNode> adjacentNodes = getAdjacent(current);
            for(int i = 0; i< adjacentNodes.size(); i++){                
                StarNode currentAdj = adjacentNodes.get(i);
                if(!openList.contains(currentAdj)){
                    currentAdj.setPrevious(current);
                    currentAdj.sethCosts(nodes[start.getX()][start.getY()], nodes[end.getX()][end.getY()]);
                    if(isDirectionChange(current, currentAdj) && !currentAdj.isTeleport()){
                        currentAdj.setMovementPanelty(penalty);
                    }
                    currentAdj.setgCosts(current);
                    openList.add(currentAdj);
                }else{
                    if(currentAdj.getgCosts() > currentAdj.calculategCosts(current)){
                        currentAdj.setPrevious(current);
                        currentAdj.setgCosts(current);
                    }
                }
            }
            if(openList.isEmpty()){
                return new LinkedList<>();
            }
        }
        return null;
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
    public boolean isDirectionChange(StarNode current, StarNode next){
        if(current.getPrevious() == null)
            return false;
        boolean res = current.getXPosition() == current.getPrevious().getXPosition() && current.getPrevious().getXPosition() == next.getXPosition() || current.getYPosition() == current.getPrevious().getYPosition() && current.getPrevious().getYPosition() == next.getYPosition();
        return res;
    }

    private List<StarNode> calcPath(StarNode start, StarNode goal) {
        LinkedList<StarNode> path = new LinkedList<StarNode>();
        StarNode curr = goal;
        boolean done = false;
        while (!done) {
            path.addFirst(curr);
            curr = (StarNode) curr.getPrevious();

            if (curr.equals(start)) {
                done = true;
            }
        }
        return path;
    }
    private StarNode lowestFInOpen() {
        StarNode cheapest = openList.get(0);
        for (int i = 0; i < openList.size(); i++) {            
            if (openList.get(i).getfCosts() < cheapest.getfCosts()) {
                cheapest = openList.get(i);                
            }
        }
        return cheapest;
    }

    public void addTeleport(Point teleport){
        teleporters.add(teleport);
    }
}
