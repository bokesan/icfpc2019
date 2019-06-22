package icfpc2019.pathfinder;

import java.util.LinkedList;
import java.util.List;

import icfpc2019.Point;

public class StarNode {
    private int x;
    private int y;
    private StarNode previous;
    private int gCosts;
    private int hCosts;
    private int movementPenalty = 0;
    private boolean isWalkable;
    private boolean isTeleport;

    protected static final int BASICMOVEMENTCOST = 10;

    public int getXPosition(){
        return x;
    }
    public int getYPosition(){
        return y;        
    }
    public Point getAsPoint(){
        return Point.of(x, y);
    }
    public StarNode(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void setPrevious(StarNode node){
        this.previous = node;
    }
    //uses euclidean distance
	public void sethCosts(StarNode starNode) {
        int dx = absolute(this.getXPosition() - starNode.getXPosition());
        int dy = absolute(this.getYPosition() - starNode.getYPosition());
        this.hCosts = (int) (BASICMOVEMENTCOST * Math.sqrt(dx * dx + dy * dy));
    }
    public void sethCosts(StarNode start, StarNode goal){
        int dx1 = this.getXPosition() - goal.getXPosition();
        int dy1 = this.getYPosition() - goal.getYPosition();
        int dx2 = start.getXPosition() - goal.getXPosition();
        int dy2 = start.getYPosition() - goal.getYPosition();
        int cross = absolute(dx1*dy2 - dx2*dy1);
        this.hCosts += cross*0.001;
    }
    private void setgCosts(int gCosts) {               
        this.gCosts = gCosts + movementPenalty;                
    }
    public void setgCosts(StarNode previousNode, int basicCost) {
        setgCosts(previousNode.getgCosts() + basicCost);
    }
	public void setgCosts(StarNode previousNode) { 
        setgCosts(previousNode, BASICMOVEMENTCOST);
    }
    public void setMovementPanelty(int movementPanelty) {
        this.movementPenalty = movementPanelty;
    }
    public void setIsWalkable(boolean isWalkable){
        this.isWalkable = isWalkable;
    }
	public int getgCosts() {
		return gCosts;
    }
    public int gethCosts(){
        return hCosts;
    }
   

	public int calculategCosts(StarNode current) {
		return current.getgCosts() + BASICMOVEMENTCOST + movementPenalty;
	}
	public StarNode getPrevious() {
		return previous;
	}
	public int getfCosts() {
        if(this.isTeleport)
           return BASICMOVEMENTCOST;
		return gCosts + hCosts;
	}
	public boolean isWalkable() {
		return isWalkable;
    }
    public boolean isTeleport(){
        return isTeleport;
    }
	public void setIsDiagonaly(boolean b) {
    }
    
    private int absolute(int a) {
        return a > 0 ? a : -a;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
	public void setIsTeleport(boolean isTeleport) {
        this.isTeleport = isTeleport;
	}
}