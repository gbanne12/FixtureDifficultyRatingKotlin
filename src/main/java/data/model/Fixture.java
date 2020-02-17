package data.model;

public class Fixture {
    public int team_a;
    public int team_h;
    public int team_h_difficulty;
    public int team_a_difficulty;

    public Fixture(int team_h, int team_h_difficulty, int team_a, int team_a_difficulty) {
        this.team_h = team_h;
        this.team_h_difficulty = team_h_difficulty;
        this.team_a = team_a;
        this.team_a_difficulty = team_a_difficulty;
    }
}
