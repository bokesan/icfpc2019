package icfpc2019;

import java.util.List;

public interface Solver {

    void init(ProblemDesc problem);
    void init(ProblemDesc problem, String shoppinglist);
    List<ActionSequence> solve();
}
