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

public class FixtureDifficultyCalculator {

    private List<Team> teamList;

    public FixtureDifficultyCalculator() throws IOException {
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

    public List<Footballer> getDifficultyTotal(List<Footballer> footballers, int gameWeek, int weeksToEvaluate) throws IOException {
        footballers.sort(new SortByTeamId());

        for (int i = 0; i < weeksToEvaluate; i++) {
            FantasyPLService fplService = new FantasyPLService();
            JSONArray fixturesArray = fplService.getFixturesArray(gameWeek + i);
            List<Fixture> fixtures = getFixturesFromArray(fixturesArray);

            for (Fixture fixture : fixtures) {
                int homeTeamId = fixture.team_h;
                int oppositionRating = fixture.team_a_difficulty;
                setOppositionNameAndDifficulty(footballers, homeTeamId, oppositionRating);

                int awayTeamId = fixture.team_a;
                oppositionRating = fixture.team_h_difficulty;
                setOppositionNameAndDifficulty(footballers, awayTeamId, oppositionRating);
            }
        }
        return footballers;
    }

    private void setOppositionNameAndDifficulty(List<Footballer> footballers, int teamId, int oppositionRating) {
        Footballer homeFootballer = new Footballer();
        homeFootballer.setTeamId(teamId);
        int result = Collections.binarySearch(footballers, homeFootballer, new SortByTeamId());
        if (result >= 0) {
            setOppositionNameAndDifficulty(footballers.get(result), teamId, oppositionRating);
            ListIterator<Footballer> iterator = footballers.listIterator(result);
            while (iterator.next().getTeamId() == teamId) {
                setOppositionNameAndDifficulty(footballers.get(iterator.nextIndex() - 1), teamId, oppositionRating);
            }
        }
    }

    public void setOppositionNameAndDifficulty(Footballer footballer, int teamId, int oppositionRating) {
        Opponent opponent = new Opponent();
        opponent.setTeamId(teamId);
        for (Team team : teamList) {
            if (teamId == team.id) {
                opponent.setName(team.short_name);
            }
        }
        opponent.setDifficultyRating(oppositionRating);
        
        List<Opponent> opponentList = footballer.getOpponentList();
        opponentList.add(opponent);
        footballer.setOpponentList(opponentList);
        int currentDifficultyScore = footballer.getDifficultyTotal();
        footballer.setDifficultyTotal(currentDifficultyScore + opponent.getDifficultyRating());
    }

    private List<Fixture> getFixturesFromArray(JSONArray fixturesArray) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Fixture> fixturesAdapter = moshi.adapter(Fixture.class);
        List<Fixture> fixtures = new ArrayList<>();
        for (int i = 0; i < fixturesArray.length(); i++) {
            Fixture fixture = fixturesAdapter.fromJson(fixturesArray.get(i).toString());
            fixtures.add(fixture);
        }
        return fixtures;
    }

    class SortByTeamId implements Comparator<Footballer> {
        public int compare(Footballer a, Footballer b) {
            return Integer.compare(a.getTeamId(), b.getTeamId());
        }
    }
}
