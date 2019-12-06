package json;

public class Event {
    public final int id;
    public final String name;
    public final Boolean finished;

    public Event(int id, String name, Boolean finished) {
        this.id = id;
        this.name = name;
        this.finished = finished;
    }
}
