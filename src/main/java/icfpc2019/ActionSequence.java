package icfpc2019;

public class ActionSequence {

    private final StringBuilder encoding = new StringBuilder(1000);

    private int length;

    public void append(Action action) {
        encoding.append(action);
        length++;
    }

    public void append(ActionType actionType, Point point) {
        encoding.append(actionType).append(point);
        length++;
    }

    public int getLength() {
        return length;
    }

    public String getEncoding() {
        return encoding.toString();
    }

}
