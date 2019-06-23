package icfpc2019;

public interface Solver {

    void init(ProblemDesc problem);
    void init(ProblemDesc problem, String shoppinglist);
    String solve();
}
