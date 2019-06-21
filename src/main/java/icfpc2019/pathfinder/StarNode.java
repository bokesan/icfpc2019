package icfpc2019.pathfinder;

import java.util.Objects;

public class StarNode {
    private int x;
    private int y;
    private StarNode previous;
    private int gCosts;
    private int hCosts;
    private int movementPanelty = 0;
    private boolean isWalkable;

    protected static final int BASICMOVEMENTCOST = 10;

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
        previous = node;
    }
	public void sethCosts(StarNode starNode) {
        hCosts = absolute(this.getXPosition() - starNode.getXPosition()
                    + absolute(this.getYPosition() - starNode.getYPosition())) * BASICMOVEMENTCOST;
    }
    private void setgCosts(int gCosts) {
        this.gCosts = gCosts + movementPanelty;        
    }
    public void setgCosts(StarNode previousNode, int basicCost) {
        setgCosts(previousNode.getgCosts() + basicCost);
    }
	public void setgCosts(StarNode previousNode) {        
        setgCosts(previousNode, BASICMOVEMENTCOST);
    }
    public void setMovementPanelty(int movementPanelty) {
        this.movementPanelty = movementPanelty;
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
		return current.getgCosts() + BASICMOVEMENTCOST + movementPanelty;
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
	public void setIsDiagonaly(boolean b) {
    }
    
    private int absolute(int a) {
        return a > 0 ? a : -a;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}