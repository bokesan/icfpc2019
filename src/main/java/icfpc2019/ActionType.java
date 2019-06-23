package icfpc2019;

public enum ActionType {
    W, //move up
    A, //move left
    S, //move down
    D, //move right
    Z, //do nothing
    E, //turn 90° clockwise
    Q, //turn 90° anti-clockwise
    F, //attach fast wheel
    R, //install beacon
    L, // use drill
    B, // extend new arm, with coordinates
    T, // teleport, with coordinates
    C  // Clone Bot
}
