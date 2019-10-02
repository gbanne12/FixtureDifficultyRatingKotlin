package squad.score;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import model.Footballer;
import model.Opponent;
import model.json.Fixture;
import model.json.Team;
import org.json.JSONArray;
import service.fpl.FantasyPLService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Footballer> getDifficulty(List<Footballer> footballers, int gameWeek, int weeksToEvaluate) throws IOException {
        for (int i = 0; i < weeksToEvaluate; i++) {
            FantasyPLService fplService = new FantasyPLService();
            JSONArray fixturesArray = fplService.getFixturesArray(gameWeek + i);
            List<Fixture> fixtures = getFixturesFromArray((fixturesArray));

            for (Footballer footballer : footballers) {
                for (Fixture fixture : fixtures) {
                    if (footballer.getTeamId() == fixture.team_a) {
                        setOppositionNameAndDifficulty(footballer, fixture, false);
                        break;
                    } else if (footballer.getTeamId() == fixture.team_h) {
                        setOppositionNameAndDifficulty(footballer, fixture, true);
                        break;
                    }
                }
            }
        }
        return footballers;
    }

    private void setOppositionNameAndDifficulty(Footballer footballer, Fixture fixture, boolean isFootballerAtHome) {
        Opponent opponent = new Opponent();
        if (isFootballerAtHome) {
            opponent.setTeamId(fixture.team_a);
            opponent.setDifficultyRating(fixture.team_h_difficulty);
            for (Team team : teamList) {
                if (fixture.team_a == team.id) {
                    opponent.setName(team.short_name);
                }
            }
        } else {
            opponent.setTeamId(fixture.team_h);
            opponent.setDifficultyRating(fixture.team_a_difficulty);
            for (Team team : teamList) {
                if (fixture.team_a == team.id) {
                    opponent.setName(team.short_name);
                }
            }
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
}
