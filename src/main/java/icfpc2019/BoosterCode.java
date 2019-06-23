package icfpc2019;

public enum BoosterCode {

    B(1000),
    F(300),
    L(700),
    X(0),
    R(1200),
    C(2000);

    private int numVal;
    BoosterCode(int numVal){
        this.numVal = numVal;
    }
    public int getNumVal(){
        return numVal;
    }
}
