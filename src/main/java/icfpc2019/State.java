package icfpc2019;

import java.util.ArrayList;
import java.util.List;

class State {

    private List<BoosterLocation> gridBoosters;
    private List<BoosterCode> availableBoosters = new ArrayList<>();
    private List<BoosterCode> mildlyWarmBoosters = new ArrayList<>();
    private List<BoosterCode> tooHotBoosters = new ArrayList<>();
    private List<Point> toVisit; // FIXME: Performance: can we use a faster data structure for this?

    State(Grid grid, List<BoosterLocation> boosters) {
        this.gridBoosters = boosters;
        this.toVisit = grid.getFreeSquares();
    }
    State(Grid grid, List<BoosterLocation> boosters, String shoppingList){
        this(grid, boosters);
        availableBoosters.addAll(checkoutShoppinglist(shoppingList));
    }
    
    private List<BoosterCode> checkoutShoppinglist(String shoppingList){
        char[] items = shoppingList.toCharArray();
        List<BoosterCode> boughtItems = new ArrayList<>();
        for(char item : items){            
            try{                
                boughtItems.add(BoosterCode.valueOf(String.valueOf(item)));
            }catch(IllegalArgumentException illegalArgument){
                //not contained
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

    List<Point> getUnwrappedPoints() {
        return new ArrayList<>(toVisit);
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

    List<Point> getBoosterLocations(BoosterCode boosterCode) {
        List<Point> result = new ArrayList<>();
        for (BoosterLocation location : gridBoosters) {
            if (location.getBoosterCode() == boosterCode) result.add(location.getPoint());
        }
        return result;
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
}
