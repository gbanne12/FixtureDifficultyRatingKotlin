package fpl.selection;

import data.Repository;
import data.model.Fixture;
import data.model.Team;
import exception.NoFplResponseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MockRepository implements Repository {

    private final String JSON_DIRECTORY = "./src/test/resources/json/";

    @Override
    public JSONArray getPicks(int teamId, int gameWeek) throws NoFplResponseException {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(JSON_DIRECTORY + "picks.json")));
        } catch (IOException e) {
            throw new NoFplResponseException(e.getMessage(), e);
        }
        JSONObject json = new JSONObject(content);
        return json.getJSONArray("picks");
    }

    @Override
    public JSONArray getElements() throws NoFplResponseException {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(JSON_DIRECTORY + "static.json")));
        } catch (IOException e) {
            throw new NoFplResponseException(e.getMessage(), e);
        }
        JSONObject json = new JSONObject(content);
        return json.getJSONArray("elements");
    }

    @Override
    public List<Fixture> getFixtures(int gameWeek) {
        List<Fixture> fixtures = new ArrayList<>();
        fixtures.add(new Fixture(10, 2, 3, 5));
        fixtures.add(new Fixture(1, 2, 19, 4));
        fixtures.add(new Fixture(7, 2, 18, 2));
        fixtures.add(new Fixture(15, 2, 14, 3));
        fixtures.add(new Fixture(16, 2, 13, 2));
        fixtures.add(new Fixture(20, 2, 4, 3));
        fixtures.add(new Fixture(5, 4, 17, 3));
        fixtures.add(new Fixture(6, 2, 8, 4));
        fixtures.add(new Fixture(12, 4, 11, 4));
        fixtures.add(new Fixture(9, 2, 2, 4));
        fixtures.add(new Fixture(11, 4, 1, 5));
        return fixtures;
    }

    @Override
    public List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(1, "ARS", "Arsenal"));
        teams.add(new Team(2, "AST", "Aston Villa"));
        teams.add(new Team(3, "BOU", "Bournemouth"));
        teams.add(new Team(4, "BHA", "Brighton"));
        teams.add(new Team(5, "BUR", "Burnley"));
        teams.add(new Team(6, "CHE", "Chelsea"));
        teams.add(new Team(7, "CRY", "Crystal Palace"));
        teams.add(new Team(8, "EVE", "Everton"));
        teams.add(new Team(9, "LEI", "Leicester"));
        teams.add(new Team(10, "LIV", "Liverpool"));
        teams.add(new Team(11, "MCI", "Man City"));
        teams.add(new Team(12, "MAN", "Manchester United"));
        teams.add(new Team(13, "NEW", "Newcastle"));
        teams.add(new Team(14, "NOR", "Norwich"));
        teams.add(new Team(15, "SHE", "Sheffield United"));
        teams.add(new Team(16, "SOU", "Southampton"));
        teams.add(new Team(17, "TOT", "Spurs"));
        teams.add(new Team(18, "WAT", "Watford"));
        teams.add(new Team(19, "WHU", "West Ham United"));
        teams.add(new Team(20, "WOL", "Wolves"));
        return teams;
    }

    @Override
    public int getGameWeek() {
        return 29;
    }
}
