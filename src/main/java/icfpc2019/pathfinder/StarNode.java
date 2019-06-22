package icfpc2019.pathfinder;

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