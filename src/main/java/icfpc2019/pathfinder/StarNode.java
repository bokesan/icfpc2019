package icfpc2019.pathfinder;

import java.util.Objects;

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
    protected static final int DIAGONALMOVEMENTCOST = 14;

    public int getXPosition(){
        return x;
    }
    public int getYPosition(){
        return y;        
    }
    public StarNode(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void setPrevious(StarNode node){
        this.previous = node;
    }
	public void sethCosts(StarNode starNode) {
        if(this.isTeleport)
            this.hCosts = BASICMOVEMENTCOST;
            
        this.hCosts = absolute(this.getXPosition() - starNode.getXPosition()
                    + absolute(this.getYPosition() - starNode.getYPosition())) * BASICMOVEMENTCOST;
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