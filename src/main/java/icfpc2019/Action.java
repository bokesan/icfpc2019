package icfpc2019;

public class Action {

    final ActionType type;
    final Point point;

    public Action(ActionType type, Point point) {
        this.type = type;
        this.point = point;
    }

    final static Action W = new Action(ActionType.W, null);
    final static Action A = new Action(ActionType.A, null);
    final static Action S = new Action(ActionType.S, null);
    final static Action D = new Action(ActionType.D, null);
    final static Action Z = new Action(ActionType.Z, null);
    final static Action E = new Action(ActionType.E, null);
    final static Action Q = new Action(ActionType.Q, null);
    final static Action F = new Action(ActionType.F, null);
    final static Action R = new Action(ActionType.R, null);
    final static Action L = new Action(ActionType.L, null);
    final static Action B = new Action(ActionType.B, null);
    final static Action T = new Action(ActionType.T, null);
    final static Action C = new Action(ActionType.C, null);

    public String toString() {
        if (point != null) {
            return type.toString() + point.toString();
        }
        return type.toString();
    }
}