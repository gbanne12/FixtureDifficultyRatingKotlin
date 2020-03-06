package data;

import data.model.*;
import exception.NoFplResponseException;
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
    private List<Footballer> footballers = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();

    @Override
    public List<Element> getElements() throws NoFplResponseException {
        if (!elements.isEmpty()) {
            return elements;
        }
        elements = new ElementsDao().getAll();
        return elements;
    }

    @Override
    public List<Footballer> getFootballers(int managerId, int gameWeek) throws IOException {
        if (!footballers.isEmpty()) {
            return footballers;
        }
        JSONArray picks = new PicksDao().getPicks(managerId, gameWeek - 1);
        footballers = new FootballerDao().getAll(picks);
        List<Element> elements = getElements();
        for (Element e : elements) {
            for (Footballer f : footballers) {
                if (e != null && e.id == f.getId()) {
                    f.setTeamId(e.team);
                    f.setWebName(e.web_name);
                }
            }
        }
        return footballers;
    }

    /**
     * Get the fixtures for a given 'gameweek'
     * Note: a gameweek is in no way tied to a calendar week
     * @param gameWeek the 'week' number of the fixtures
     * @return List of fixtures containing the home and away team IDs
     * @throws IOException when response cannot be obtained from the endpoint
     */
    @Override
    public List<Fixture> getFixtures(int gameWeek) throws NoFplResponseException {
        boolean matchesCurrentFixtures = (gameWeek == getGameWeek() && !fixtures.isEmpty());
        if (matchesCurrentFixtures) {
            return fixtures;
        }
        fixtures = new FixturesDao().getAll(gameWeek);
        return fixtures;
    }

    @Override
    public List<Team> getTeams() throws NoFplResponseException {
        if (!teams.isEmpty()) {
            return teams;
        }
        teams = new TeamDao().getTeams();
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
    public int getGameWeek() throws NoFplResponseException {
        if (week > 0) {
            return week;
        }
        week = new EventDao().getCurrentWeek();
        return week;
    }
}
