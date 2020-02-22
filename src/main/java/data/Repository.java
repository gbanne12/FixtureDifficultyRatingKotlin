package data;

import data.model.Fixture;
import data.model.Team;
import exception.NoFplResponseException;
import org.json.JSONArray;

import java.util.List;

public interface Repository {

    JSONArray getPicks(int teamId, int gameWeek) throws NoFplResponseException;

    JSONArray getElements() throws NoFplResponseException;

    List<Fixture> getFixtures(int gameWeek) throws NoFplResponseException;

    List<Team> getTeams() throws NoFplResponseException;

    int getGameWeek() throws NoFplResponseException;
}
