package fpl.score;

import data.Repository;
import data.model.*;
import exception.NoFplResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FixtureCalculator {

    private Repository repo;

    public FixtureCalculator(Repository repository) {
        repo = repository;
    }

    public List<Footballer> getFixturesDifficulty(int managerId, int weeksToCalculate) throws IOException {
        List<Footballer> footballers = repo.getFootballers(managerId, repo.getGameWeek());
        for (int i = 0; i < weeksToCalculate; i++) {
            updateOpposition(footballers, repo.getGameWeek() + i);
        }
        return footballers;
    }

    public void updateOpposition(List<Footballer> footballers, int gameWeek) throws IOException {
        List<Footballer> playingFootballers = new ArrayList<>();
        List<Fixture> gameWeekFixtures = repo.getFixtures(gameWeek);
        FootballerDao footballerDao = new FootballerDao();

        for (Fixture fixture : gameWeekFixtures) {
            int homeTeamId = fixture.team_h;
            int homeTeamFixtureDifficulty = fixture.team_h_difficulty;
            int awayTeamId = fixture.team_a;
            int awayTeamFixtureDifficulty = fixture.team_a_difficulty;


            List<Footballer> homeFootballers = footballerDao.getByTeamId(footballers, homeTeamId);
            for (Footballer f : homeFootballers) {
                footballerDao.update(footballers, f.getId(), buildOpponent(awayTeamId, homeTeamFixtureDifficulty));
            }

            List<Footballer> awayFootballers = footballerDao.getByTeamId(footballers, awayTeamId);
            for (Footballer f : awayFootballers) {
                footballerDao.update(footballers, f.getId(), buildOpponent(homeTeamId, awayTeamFixtureDifficulty));
            }

            playingFootballers.addAll(homeFootballers);
            playingFootballers.addAll(awayFootballers);
        }

        List<Footballer> nonPlayingFootballers = footballerDao.getInverse(footballers, playingFootballers);
        for (Footballer f : nonPlayingFootballers) {
            footballerDao.update(footballers, f.getId(), buildOpponent(-1, 0));
        }
    }

    private Opponent buildOpponent(int oppositionId, int difficultyRating) throws NoFplResponseException {
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
        return opponent;
    }
}
