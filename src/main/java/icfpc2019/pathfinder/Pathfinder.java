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
        for(int i = 0; i < grid.getFields().length; i++){
            for(int x = 0; x < grid.getFields()[i].length ; x++){
                StarNode temp = new StarNode(x, i);
                temp.setIsWalkable(grid.isFree(Point.of(x, i)));
                nodes[x][i] = temp;
            }
        }
    }
    
    public final List<StarNode> findPath(int oldX, int oldY, int newX, int newY){
        openList = new LinkedList<StarNode>();
        closedList = new LinkedList<StarNode>();        
        openList.add(nodes[oldX][oldY]);
        done = false;
        StarNode current;
        while(!done){
            current = lowestFInOpen();
            closedList.add(current);
            openList.remove(current);
            if((current.getXPosition() == newX) && (current.getYPosition()==newY)){
                return calcPath(nodes[oldX][oldY], current);
            }            
            List<StarNode> adjacentNodes = getAdjacent(current);
            for(int i = 0; i< adjacentNodes.size(); i++){                
                StarNode currentAdj = adjacentNodes.get(i);
                if(!openList.contains(currentAdj)){
                    currentAdj.setPrevious(current);
                    currentAdj.sethCosts(nodes[newX][newY]);
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
                return new LinkedList<StarNode>();
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
