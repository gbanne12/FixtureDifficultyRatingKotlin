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

public class DifficultyCalculator {

    private Repository repo;

    public DifficultyCalculator(Repository repository) {
        repo = repository;
    }

    public List<Footballer> difficultyRatingForWeeks(Selection selection, int weeksToCalculate)
            throws NoFplResponseException {
        List<Footballer> footballers = selection.get();
        for (int i = 0; i < weeksToCalculate; i++) {
            footballers = difficultyRating(selection, repo.getGameWeek() + i);
        }
        return footballers;
    }

    public List<Footballer> difficultyRating(Selection selection, int gameWeek) throws NoFplResponseException {
        List<Footballer> footballers = selection.get();
        List<Fixture> gameWeekFixtures = repo.getFixtures(gameWeek);
        List<Footballer> playingFootballers = new ArrayList<>();

        for (Fixture fixture : gameWeekFixtures) {
            int homeTeamId = fixture.team_h;
            int homeTeamFixtureDifficulty = fixture.team_h_difficulty;
            int awayTeamId = fixture.team_a;
            int awayTeamFixtureDifficulty = fixture.team_a_difficulty;

            List<Footballer> homeFootballers = selection.collectByTeamId(homeTeamId);
            for (Footballer footballer : homeFootballers) {
                Opponent opponent = buildOpponent(awayTeamId, homeTeamFixtureDifficulty);
                footballer.getOpponentList().add(opponent);
            }

            List<Footballer> awayFootballers = selection.collectByTeamId(awayTeamId);
            for (Footballer footballer : awayFootballers) {
                Opponent opponent = buildOpponent(homeTeamId, awayTeamFixtureDifficulty);
                footballer.getOpponentList().add(opponent);
            }

            playingFootballers.addAll(homeFootballers);
            playingFootballers.addAll(awayFootballers);
        }

        List<Footballer> nonPlayingFootballers = selection.collectInverse(playingFootballers);
        for (Footballer footballer : nonPlayingFootballers) {
            Opponent opponent = buildOpponent(-1, 0);  //-1 to indicate no opponent
            footballer.getOpponentList().add(opponent);
        }
        return footballers;
    }

    private Opponent buildOpponent(int opponentId, int rating) throws NoFplResponseException {
        Opponent opponent = new Opponent();
        opponent.setDifficultyRating(rating);

        if (opponentId == -1) {
            opponent.setName("NOT PLAYING");
        } else {
            opponent.setTeamId(opponentId);
            List<Team> teamList = repo.getTeams();
            for (Team team : teamList) {
                if (opponentId == team.id) {
                    opponent.setName(team.short_name);
                }
            }
        }
        return opponent;
    }
}
