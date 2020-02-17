package fpl.score;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import data.model.Fixture;
import data.model.Team;
import fpl.FantasyPLService;
import fpl.teams.fantasy.Selection;
import model.Footballer;
import model.Opponent;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DifficultyRating {
    private List<Team> teamList;

    public DifficultyRating() throws IOException {
        FantasyPLService fplService = new FantasyPLService();
        JSONArray teamsArray = fplService.getTeamsArray();
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Team> teamsAdapter = moshi.adapter(Team.class);
        teamList = new ArrayList<>();
        for (int j = 0; j < teamsArray.length(); j++) {
            teamList.add(teamsAdapter.fromJson(teamsArray.get(j).toString()));
        }
    }

    public List<Footballer> getOpponentDifficulty(Selection selection, int gameWeek, int weeksToCalculate) throws IOException{
        List<Footballer> footballers = selection.get();
        for(int i = 0; i < weeksToCalculate; i++) {
            footballers = getOpponentDifficulty(selection, gameWeek + i);
        }
        return footballers;
    }

    public List<Footballer> getOpponentDifficulty(Selection selection, int gameWeek) throws IOException {
        List<Footballer> footballers = selection.get();
        FantasyPLService fplService = new FantasyPLService();
        JSONArray fixturesArray = fplService.getFixturesArray(gameWeek);
        List<Fixture> fixtures = getFixturesList(fixturesArray);
        List<Footballer> footballersWithFixture = new ArrayList<>();

        for (Fixture fixture : fixtures) {
            int homeTeamId = fixture.team_h;
            int homeTeamFixtureDifficulty = fixture.team_h_difficulty;
            int awayTeamId = fixture.team_a;
            int awayTeamFixtureDifficulty = fixture.team_a_difficulty;


            List<Footballer> homeFootballers = filterFootballersByTeamId(footballers, homeTeamId);
            for (Footballer footballer : homeFootballers) {
                footballer.setOpponentList(getOpponnentList(footballer, awayTeamId, homeTeamFixtureDifficulty));
            }
            footballersWithFixture.addAll(homeFootballers);

            List<Footballer> awayFootballers = filterFootballersByTeamId(footballers, awayTeamId);
            for (Footballer footballer : awayFootballers) {
                footballer.setOpponentList(getOpponnentList(footballer, homeTeamId, awayTeamFixtureDifficulty));
            }
            footballersWithFixture.addAll(awayFootballers);
        }

        Set<Integer> footballersWithFixtureIds = footballersWithFixture.stream()
                .map(Footballer::getId)
                .collect(Collectors.toSet());
        List<Footballer> footballersWithNoFixture = footballers.stream()
                .filter(footballer -> !footballersWithFixtureIds.contains(footballer.getId()))
                .collect(Collectors.toList());

        for (Footballer footballer : footballersWithNoFixture) {
            Opponent opponent = new Opponent();
            opponent.setName("NOT PLAYING");
            opponent.setDifficultyRating(0);
            List<Opponent> opponentList = footballer.getOpponentList();
            opponentList.add(opponent);
            footballer.setOpponentList(opponentList);
        }
        return footballers;
    }

    private List<Footballer> filterFootballersByTeamId(List<Footballer> footballers, int teamId) {
        return footballers.stream()
                .filter(x -> teamId == x.getTeamId())
                .limit(3)
                .collect(Collectors.toList());
    }

    private List<Fixture> getFixturesList(JSONArray fixturesArray) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Fixture> fixturesAdapter = moshi.adapter(Fixture.class);
        List<Fixture> fixtures = new ArrayList<>();
        for (int i = 0; i < fixturesArray.length(); i++) {
            Fixture fixture = fixturesAdapter.fromJson(fixturesArray.get(i).toString());
            fixtures.add(fixture);
        }
        return fixtures;
    }

    private List<Opponent> getOpponnentList(Footballer footballer, int opponentId, int oppositionRating) throws IOException {
        Opponent opponent = new Opponent();
        opponent.setTeamId(opponentId);
        for (Team team : teamList) {
            if (opponentId == team.id) {
                opponent.setName(team.short_name);
            }
        }
        List<Opponent> opponentList = footballer.getOpponentList();
        opponent.setDifficultyRating(oppositionRating);
        opponentList.add(opponent);
        footballer.setOpponentList(opponentList);
        return opponentList;
    }
}
