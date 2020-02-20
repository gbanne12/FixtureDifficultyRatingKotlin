package data;

import data.model.*;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for data retrieved directly from the FPL endpoints
 * i.e. held in memory only, not saved anywhere.
 */
public class TransientRepository implements Repository {

    private int week;
    private List<Fixture> fixtures = new ArrayList<>();
    private List<Team> teams = new ArrayList<>();
    private JSONArray picks = new JSONArray();
    private JSONArray elements = new JSONArray();

    @Override
    public JSONArray getElements() throws IOException {
        if (elements.isEmpty()) {
            elements = new ElementsDao().getElements();
        }
        return elements;
    }

    @Override
    public JSONArray getPicks(int teamId, int gameWeek) throws IOException {
        if (picks.isEmpty()) {
            picks = new PicksDao().getPicks(teamId,gameWeek - 1);
        }
        return picks;
    }

    @Override
    public List<Fixture> getFixtures() throws IOException{
        if (fixtures.isEmpty()) {
            fixtures = new FixturesDao().getAllFixtures(getGameWeek());
        }
        return fixtures;
    }

    @Override
    public List<Team> getTeams() throws IOException {
        if (teams.isEmpty()) {
            teams = new TeamDao().getTeams();
        }
        return teams;
    }

    /**
     * Get the currently active game week number
     * Will return the first unfinished game week, so an in-progress but unfinished week will be returned
     *
     * @return number of the first unfinished game week
     * @throws IOException
     */
    @Override
    public int getGameWeek() throws IOException {
        if (week == 0) {
            week = new EventDao().getCurrentWeek();
        }
        return week;
    }
}
