package fpl.score;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import fpl.FantasyPLService;
import json.Fixture;
import json.Team;
import model.Footballer;
import model.Opponent;
import org.json.JSONArray;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FixtureDifficultyCalculator {
    private List<Team> teamList;

    public List<Footballer> getDifficultyTotal(List<Footballer> footballers, int gameWeek) throws IOException {

            FantasyPLService fplService = new FantasyPLService();
            JSONArray fixturesArray = fplService.getFixturesArray(gameWeek);
            List<Fixture> fixtures = getFixturesList(fixturesArray);
            for (Fixture fixture : fixtures) {
                int homeTeamId = fixture.team_h;
                int homeTeamFixtureDifficulty = fixture.team_h_difficulty;
                int awayTeamId = fixture.team_a;
                int awayTeamFixtureDifficulty = fixture.team_a_difficulty;

                List<Footballer> homeFootballers = filterFootballersByTeamId(footballers, homeTeamId);
                for (Footballer footballer : homeFootballers) {
                    getOpponnentList(footballer, awayTeamId, homeTeamFixtureDifficulty);
                }

                List<Footballer> awayFootballers = filterFootballersByTeamId(footballers, awayTeamId);
                for (Footballer footballer : awayFootballers) {
                    getOpponnentList(footballer, homeTeamId, awayTeamFixtureDifficulty);
                }
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

    private List<Team> getTeamList() throws IOException {
        // Look up the team array only once
        if (teamList == null) {
            FantasyPLService fplService = new FantasyPLService();
            JSONArray teamsArray = fplService.getTeamsArray();
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Team> teamsAdapter = moshi.adapter(Team.class);
            teamList = new ArrayList<>();
            for (int j = 0; j < teamsArray.length(); j++) {
                teamList.add(teamsAdapter.fromJson(teamsArray.get(j).toString()));
            }
        }
        return teamList;
    }

    private List<Opponent> getOpponnentList(Footballer footballer, int opponentId, int oppositionRating) throws IOException {
        Opponent opponent = new Opponent();
        opponent.setTeamId(opponentId);
        teamList = getTeamList();
        for (Team team : teamList) {
            if (opponentId == team.id) {
                opponent.setName(team.short_name);
            }
        }
        opponent.setDifficultyRating(oppositionRating);

        List<Opponent> opponentList = footballer.getOpponentList();
        opponentList.add(opponent);
        footballer.setOpponentList(opponentList);
        return opponentList;
    }
}
