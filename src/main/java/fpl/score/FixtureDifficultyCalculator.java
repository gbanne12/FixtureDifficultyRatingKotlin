package fpl.score;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import model.Footballer;
import model.Opponent;
import json.Fixture;
import json.Team;
import org.json.JSONArray;
import fpl.FantasyPLService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

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
        for (int i = 0; i < weeksToEvaluate; i++) {
            FantasyPLService fplService = new FantasyPLService();
            JSONArray fixturesArray = fplService.getFixturesArray(gameWeek + i);
            List<Fixture> fixtures = getFixturesFromArray(fixturesArray);

                for (Fixture fixture : fixtures) {
                    footballers.sort(new SortByTeamId());
                    Footballer matchingTeamId = new Footballer();
                    matchingTeamId.setTeamId(fixture.team_h);
                   int index = Collections.binarySearch(footballers, matchingTeamId, new SortByTeamId());
                   if (index >= 0) {
                       setOppositionNameAndDifficulty(footballers.get(index), fixture, true);
                   }

                    matchingTeamId.setTeamId(fixture.team_a);
                    int awayIndex = Collections.binarySearch(footballers, matchingTeamId, new SortByTeamId());
                        if (awayIndex >= 0) {
                            setOppositionNameAndDifficulty(footballers.get(awayIndex), fixture, false);

                        }
                }
            }
        return footballers;
    }

    private void setOppositionNameAndDifficulty(Footballer footballer, Fixture fixture, boolean isFootballerAtHome) {
        int awayTeam = fixture.team_a;
        int homeTeam = fixture.team_h;
        int difficultyForHomeTeam = fixture.team_h_difficulty;
        int difficultyForAwayTeam = fixture.team_a_difficulty;

        Opponent opponent = new Opponent();
        if (isFootballerAtHome) {
            opponent.setTeamId(awayTeam);
            for (Team team : teamList) {
                if (awayTeam == team.id) {
                    opponent.setName(team.short_name);
                }
            }
            opponent.setDifficultyRating(difficultyForHomeTeam);

        } else {
            opponent.setTeamId(homeTeam);
            for (Team team : teamList) {
                if (homeTeam == team.id) {
                    opponent.setName(team.short_name);
                }
            }
            opponent.setDifficultyRating(difficultyForAwayTeam);
        }

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

    class SortByTeamId implements Comparator<Footballer>
    {
        // Used for sorting in ascending order of
        // roll name
        public int compare(Footballer a, Footballer b)
        {
            return Integer.compare(a.getTeamId(), b.getTeamId());
        }
    }
}
