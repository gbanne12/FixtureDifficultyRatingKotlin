package model;

public class Opposition {

    private int teamId;
    private int difficultyRating;

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

    @Override
    public String toString() {
        return "team: " + teamId + " | difficulty: " + difficultyRating;

    }
}
