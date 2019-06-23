package icfpc2019;

public enum Direction {
    NORTH,
    EAST,
    WEST,
    SOUTH;

    public static Direction of(Point source, Point target) {
        if (source.getX() == target.getX()) {
            int distance = target.getY() - source.getY();
            if (distance == 1) return NORTH;
            if (distance == -1) return SOUTH;
        } else if (source.getY() == target.getY()) {
            int distance = target.getX() - source.getX();
            if (distance == 1) return EAST;
            if (distance == -1) return WEST;
        }
        return null;
    }
}