package model;

@SuppressWarnings("WeakerAccess")
public class Opponent {

    private int teamId;
    private int difficultyRating;
    private String name;

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getDifficultyRating() {
        return difficultyRating;
    }

    public void setDifficultyRating(int difficultyRating) {
        this.difficultyRating = difficultyRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "team: " + name + " | difficulty: " + difficultyRating;
    }
}
