package icfpc2019.pathfinder;


import icfpc2019.Point;

class StarNode {
    private final int x;
    private final int y;
    private StarNode previous;
    private double gCosts;
    private double hCosts;
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
        int dx = Math.abs(this.getXPosition() - starNode.getXPosition());
        int dy = Math.abs(this.getYPosition() - starNode.getYPosition());
        this.hCosts = (int) (BASICMOVEMENTCOST * Math.sqrt(dx * dx + dy * dy));
    }

    public void sethCosts(StarNode start, StarNode goal){
        int dx1 = this.getXPosition() - goal.getXPosition();
        int dy1 = this.getYPosition() - goal.getYPosition();
        int dx2 = start.getXPosition() - goal.getXPosition();
        int dy2 = start.getYPosition() - goal.getYPosition();
        int cross = Math.abs(dx1*dy2 - dx2*dy1);
        this.hCosts += cross*0.001;
    }

    private void setgCosts(double gCosts) {               
        this.gCosts = gCosts;
    }

    public void setgCosts(StarNode previousNode, double basicCost) {
        setgCosts(previousNode.getgCosts() + basicCost);
    }

	public void setgCosts(StarNode previousNode) { 
        setgCosts(previousNode, BASICMOVEMENTCOST);
    }

    public void setIsWalkable(boolean isWalkable){
        this.isWalkable = isWalkable;
    }

	public double getgCosts() {
		return gCosts;
    }

    public double gethCosts(){
        return hCosts;
    }
   

	public double calculategCosts(StarNode current) {
		return current.getgCosts() + BASICMOVEMENTCOST;
	}

	public StarNode getPrevious() {
		return previous;
	}

	public double getfCosts() {
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

    public String toString() {
        return "(" + x + "," + y + ")";
    }
	public void setIsTeleport(boolean isTeleport) {
        this.isTeleport = isTeleport;
	}
}