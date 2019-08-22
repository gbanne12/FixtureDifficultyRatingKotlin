package model.json;

public class Pick {
    public final int element;
    public final int position;
    public final int multiplier;
    public final boolean is_vice_captain;

    public Pick(int element, int position, int multiplier, boolean is_captain, boolean is_vice_captain) {
        this.element = element;
        this.position = position;
        this.multiplier = multiplier;
        this.is_vice_captain = is_vice_captain;
    }

}
