package icfpc2019;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class State {

    private List<BoosterLocation> gridBoosters;
    private List<BoosterCode> availableBoosters = new ArrayList<>();
    private List<BoosterCode> mildlyWarmBoosters = new ArrayList<>();
    private List<BoosterCode> tooHotBoosters = new ArrayList<>();
    private Set<Point> toVisit;
    private List<Point> telePortTargets = new ArrayList<>();

    State(Grid grid, List<BoosterLocation> boosters) {
        this.gridBoosters = boosters;
        this.toVisit = grid.getFreeSquares();
    }
    State(Grid grid, List<BoosterLocation> boosters, String shoppingList){
        this(grid, boosters);
        availableBoosters.addAll(checkoutShoppinglist(shoppingList));
    }

    private List<BoosterCode> checkoutShoppinglist(String shoppingList){
        List<BoosterCode> boughtItems = new ArrayList<>();
        for(char item : shoppingList.replaceAll("([\\r\\n])", "").toCharArray()) {
            try {
                boughtItems.add(BoosterCode.valueOf(String.valueOf(item)));
            } catch(IllegalArgumentException illegalArgument){
                //throw new RuntimeException("Invalid booster code: " + item);
            }
        }
        return boughtItems;
    }

    boolean mapFinished() {
        return toVisit.isEmpty();
    }

    void wrap(Point p) {
        toVisit.remove(p);
    }

    /**
     * Get unwrapped point nearest to the target by manhattan distance.
     */
    Point getFurthestUnwrapped(Point target, Point exclude) {
        Point furthest = null;
        int minDist = 0;
        for (Point p : toVisit) {
            if(exclude != null && p.equals(exclude) && toVisit.size() != 1)
                 continue;
            int dist = target.manhattanDistance(p);
            if (dist > minDist) {
                minDist = dist;
                furthest = p;
            }
        }
        return furthest;
    }
    Point getNearestUnwrapped(Point target) {
        Point nearest = null;
        int minDist = Integer.MAX_VALUE;
        for (Point p : toVisit) {           
            int dist = target.manhattanDistance(p);
            if (dist < minDist) {
                minDist = dist;
                nearest = p;
            }
        }
        return nearest;
    }


    void pickBoosterUp(Point position) {
        for (BoosterLocation booster : gridBoosters) {
            if (booster.getBoosterCode() != BoosterCode.X && booster.getPoint().equals(position)) {
                tooHotBoosters.add(booster.getBoosterCode());
                gridBoosters.remove(booster);
                break;
            }
        }
    }

    boolean boosterAvailable(BoosterCode booster) {
        return availableBoosters.contains(booster);
    }

    boolean needsWrapping(Point p) {
        return toVisit.contains(p);
    }

    boolean mapHasBooster(BoosterCode booster) {
        for (BoosterLocation location : gridBoosters) {
            if (location.getBoosterCode() == booster) return true;
        }
        return false;
    }

    int getGridBoosterCount(BoosterCode type) {
        int n = 0;
        for (BoosterLocation b : gridBoosters) {
            if (b.getBoosterCode() == type) {
                n++;
            }
        }
        return n;
    }

    List<Point> getBoosterLocations(BoosterCode boosterCode) {
        List<Point> result = new ArrayList<>();
        for (BoosterLocation location : gridBoosters) {
            if (location.getBoosterCode() == boosterCode) result.add(location.getPoint());
        }
        return result;
    }

    boolean hasSpawnPointAt(Point location) {
        for (BoosterLocation b : gridBoosters) {
            if (b.getBoosterCode() == BoosterCode.X && b.getPoint().equals(location)) {
                return true;
            }
        }
        return false;
    }

    void removeBooster(BoosterCode b) {
        if (!availableBoosters.contains(b)) throw new RuntimeException("Booster not available: " + b.name());
        availableBoosters.remove(b);
    }

    void coolBoosterDown() {
        availableBoosters.addAll(mildlyWarmBoosters);
        mildlyWarmBoosters.clear();
        mildlyWarmBoosters.addAll(tooHotBoosters);
        tooHotBoosters.clear();
    }

    void addTeleportTarget(Point position) {
        telePortTargets.add(position);
    }

    boolean isTeleportTarget(Point target) {
        return telePortTargets.contains(target);
    }

    int getNumAvailableBooster(BoosterCode code) {
        int i = 0;
        for (BoosterCode b : availableBoosters) {
            if (code == b) i++;
        }
        return i;
    }
}
