package icfpc2019;

import icfpc2019.pathfinder.DistanceMap;

import java.util.*;

class State {

    private List<BoosterLocation> gridBoosters;
    private List<BoosterCode> availableBoosters = new ArrayList<>();
    private List<BoosterCode> mildlyWarmBoosters = new ArrayList<>();
    private List<BoosterCode> tooHotBoosters = new ArrayList<>();
    private Set<Point> toVisit;
    private List<Point> telePortTargets = new ArrayList<>();
    private final DistanceMap distances;
    private final Random random = new Random();

    State(Grid grid, List<BoosterLocation> boosters) {
        this.gridBoosters = boosters;
        this.toVisit = grid.getFreeSquares();
        this.distances = new DistanceMap(grid);
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

    List<Point> getTeleportTargets(){
        return this.telePortTargets;
    }

    boolean mapFinished() {
        return toVisit.isEmpty();
    }

    void wrap(Point p) {
        toVisit.remove(p);
    }

    Point getFurthestUnwrapped(Point target, Point exclude) {
        Point furthest = null;
        int minDist = 0;
        distances.from(target);
        if (toVisit.size() == 1) {
            // make sure we still get a result
            exclude = null;
        }
        for (Point p : toVisit) {
            if (p.equals(exclude))
                 continue;
            int dist = distances.get(p);
            if (dist > minDist) {
                minDist = dist;
                furthest = p;
            }
        }
        return furthest;
    }

    /**
     * Get unwrapped point nearest to the target.
     */
    Point getNearestUnwrapped(Point target) {
        Point nearest = null;
        Point nearest2 = null;
        int minDist = Integer.MAX_VALUE;
        distances.from(target);
        for (Point p : toVisit) {
            int dist = distances.get(p);
            if (dist <= minDist) {
                if (dist < minDist) {
                    minDist = dist;
                    nearest = p;
                    nearest2 = null;
                } else if (nearest2 == null) {
                    nearest2 = p;
                }
            }
        }
        if (nearest2 == null || random.nextBoolean()) {
            return nearest;
        } else {
            return nearest2;
        }
    }


    void pickBoosterUp(Point position) {
        for (Iterator<BoosterLocation> it = gridBoosters.iterator(); it.hasNext(); ) {
            BoosterLocation booster = it.next();
            if (booster.getBoosterCode() != BoosterCode.X && booster.getPoint().equals(position)) {
                tooHotBoosters.add(booster.getBoosterCode());
                it.remove();
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
        if (!availableBoosters.remove(b))
            throw new RuntimeException("Booster not available: " + b.name());
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
