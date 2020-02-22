package data;

import data.model.Fixture;
import data.model.Team;
import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

public interface Repository {

    JSONArray getPicks(int teamId, int gameWeek) throws IOException;

    JSONArray getElements() throws IOException;

    List<Fixture> getFixtures() throws IOException;

    List<Team> getTeams() throws IOException;

    int getGameWeek() throws IOException;
}
