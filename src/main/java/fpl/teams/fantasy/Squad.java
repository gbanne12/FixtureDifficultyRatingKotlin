package fpl.teams.fantasy;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import model.Footballer;
import json.Element;
import json.Pick;
import json.Team;
import org.json.JSONArray;
import fpl.FantasyPLService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Squad {

    private List<Footballer> footballerList = new ArrayList<>();
    private List<Team> teamList;

    public Squad() throws IOException {
        // Look up the team array only once
        FantasyPLService fplService = new FantasyPLService();
        JSONArray teamsArray = fplService.getTeamsArray();
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Team> teamsAdapter = moshi.adapter(Team.class);
        teamList = new ArrayList<>();
        for (int j = 0; j < teamsArray.length(); j++) {
            teamList.add(teamsAdapter.fromJson(teamsArray.get(j).toString()));
        }
    }

    /**
     * Get a list containing the manager's squad selection for a given week
     * The first 11 items in the list are the chosen team with the last 4 items being the substitutes
     * @param week the game week the squad was selected for
     * @return a list of 15 footballerList that make up the squad
     * @throws IOException
     */
    public List<Footballer> get(int week) throws IOException {
        FantasyPLService fplService = new FantasyPLService();
        JSONArray picks = fplService.getPicksArray(week);
        footballerList = getFootballersFromArray(picks);
        JSONArray elements = fplService.getElementsArray();
        populateFootballerDetails(elements);
        return footballerList;
    }

    private List<Footballer> getFootballersFromArray(JSONArray picks) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Pick> picksAdapter = moshi.adapter(Pick.class);

        List<Footballer> footballers = new ArrayList<>();
        for (int i = 0; i < picks.length(); i++) {
            Pick pick = picksAdapter.fromJson((picks.get(i)).toString());
            if (pick != null) {
                Footballer footballer = new Footballer();
                footballer.setId(pick.element);
                footballer.setPosition(pick.position);
                footballers.add(footballer);
            }
        }
        return footballers;
    }

    private void populateFootballerDetails(JSONArray elements) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Element> elementsAdapter = moshi.adapter(Element.class);

        // add team id and footballer name
        for (int j = 0; j < elements.length(); j++) {
            Element element = elementsAdapter.fromJson((elements.get(j)).toString());
            for (Footballer footballer : footballerList) {
                if (element != null && element.id == footballer.getId()) {
                    footballer.setTeamId(element.team);
                    footballer.setWebName(element.web_name);
                }
            }
        }

        // getGamewWeek team list and add team name based on id
        for (Footballer footballer : footballerList) {
            for (Team team : teamList) {
                if (team != null && team.id == footballer.getTeamId()) {
                    footballer.setTeamName(team.short_name);
                }
            }
        }

    }
}
