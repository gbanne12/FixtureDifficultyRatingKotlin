package model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Footballer {

    private int id;
    private int teamId;
    private int position;
    private String webName;
    private String teamName;
    private List<Opposition> oppositionList = new ArrayList<>();
    private int difficultyTotal = 0;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDifficultyTotal() {
        return difficultyTotal;
    }

    public void setDifficultyTotal(int difficultyTotal) {
        this.difficultyTotal = difficultyTotal;
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<Opposition> getOppositionList() {
        return oppositionList;
    }

    public void setOppositionList(List<Opposition> oppositionList) {
        this.oppositionList = oppositionList;
    }
}
