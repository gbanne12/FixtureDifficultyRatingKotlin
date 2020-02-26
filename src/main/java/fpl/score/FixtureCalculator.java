package fpl.score;

import data.Repository;
import data.model.Fixture;
import data.model.Footballer;
import data.model.Opponent;
import data.model.Team;
import exception.NoFplResponseException;
import fpl.selection.Selection;

import java.util.ArrayList;
import java.util.List;

public class FixtureCalculator {

    private Repository repo;

    public FixtureCalculator(Repository repository) {
        repo = repository;
    }

    public void addOppositionForWeeks(Selection selection, int weeksToCalculate) throws NoFplResponseException {
        for (int i = 0; i < weeksToCalculate; i++) {
            addOpposition(selection, repo.getGameWeek() + i);
        }
    }

    public void addOpposition(Selection selection, int gameWeek) throws NoFplResponseException {
        List<Fixture> gameWeekFixtures = repo.getFixtures(gameWeek);
        List<Footballer> playingFootballers = new ArrayList<>();

        for (Fixture fixture : gameWeekFixtures) {
            int homeTeamId = fixture.team_h;
            int homeTeamFixtureDifficulty = fixture.team_h_difficulty;
            int awayTeamId = fixture.team_a;
            int awayTeamFixtureDifficulty = fixture.team_a_difficulty;

            List<Footballer> homeFootballers = selection.collectByTeamId(homeTeamId);
            addOpposition(homeFootballers, awayTeamId, homeTeamFixtureDifficulty);

            List<Footballer> awayFootballers = selection.collectByTeamId(awayTeamId);
            addOpposition(awayFootballers, homeTeamId, awayTeamFixtureDifficulty);

            playingFootballers.addAll(homeFootballers);
            playingFootballers.addAll(awayFootballers);
        }

        List<Footballer> nonPlayingFootballers = selection.collectInverse(playingFootballers);
        addOpposition(nonPlayingFootballers, -1, 0);
    }

    private void addOpposition(List<Footballer> footballers, int oppositionId, int difficultyRating)
            throws NoFplResponseException{
        Opponent opponent = new Opponent();
        opponent.setDifficultyRating(difficultyRating);

        if (oppositionId == -1) {
            opponent.setName("NO FIXTURE");
        } else {
            opponent.setTeamId(oppositionId);
            List<Team> teamList = repo.getTeams();
            for (Team team : teamList) {
                if (oppositionId == team.id) {
                    opponent.setName(team.short_name);
                }
            }
        }

        for (Footballer footballer : footballers) {
            footballer.getOpponentList().add(opponent);
        }
    }
}
