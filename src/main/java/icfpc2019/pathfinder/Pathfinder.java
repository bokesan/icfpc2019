package icfpc2019.pathfinder;

import java.util.LinkedList;
import java.util.List;
import icfpc2019.*;

public class Pathfinder{
    LinkedList<StarNode> openList;
    LinkedList<StarNode> closedList;
    LinkedList<StarNode> adjacenetNodes;
    StarNode[][] nodes;
    boolean done;    
    int width;
    int height;

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
    
    public final List<StarNode> findPath(Point start, Point end){
        openList = new LinkedList<>();
        closedList = new LinkedList<>();
        openList.add(nodes[start.getX()][start.getY()]);
        done = false;
        StarNode current;
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
                    currentAdj.sethCosts(nodes[end.getX()][end.getY()]);
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

    private List<StarNode> getAdjacent(StarNode node) {
        int x = node.getXPosition();
        int y = node.getYPosition();
        List<StarNode> adj = new LinkedList<StarNode>();

        StarNode temp;
        if (x > 0) {
            temp = nodes[x-1][y];
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }

        if (x < width) {
            temp = nodes[x+1][y];
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }

        if (y > 0) {
            temp = nodes[x][y-1];
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }

        if (y < height) {
            temp = nodes[x][y+1];
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }
        return adj;
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
}