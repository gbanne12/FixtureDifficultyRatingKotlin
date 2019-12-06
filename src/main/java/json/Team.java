package json;

public class Team {
    public final int id;
    public final String short_name;
    public final String name;

    public Team(int id, String short_name, String name) {
        this.id = id;
        this.short_name = short_name;
        this.name = name;
    }
}
