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
        List<Footballer> footballers = repo.getFootballers(managerId, repo.getGameWeek() - 1);
        for (int i = 0; i < weeksToCalculate; i++) {
            updateOpposition(footballers, repo.getGameWeek() + i);
        }
        return footballers;
    }

    public void updateOpposition(List<Footballer> footballers, int gameWeek) throws IOException {
        List<Footballer> playingFootballers = new ArrayList<>();
        FootballerDaoImpl footballerDao = new FootballerDaoImpl();

        List<Fixture> gameWeekFixtures = repo.getFixtures(gameWeek);
        for (Fixture fixture : gameWeekFixtures) {
            int homeTeamId = fixture.getTeam_h();
            int homeTeamFixtureDifficulty = fixture.getTeam_h_difficulty();
            int awayTeamId = fixture.getTeam_a();
            int awayTeamFixtureDifficulty = fixture.getTeam_a_difficulty();

            List<Footballer> homeFootballers = footballerDao.getByTeamId(footballers, homeTeamId);
            for (Footballer f : homeFootballers) {
                footballerDao.update(homeFootballers, f.getId(), buildOpponent(awayTeamId, homeTeamFixtureDifficulty));
            }

            List<Footballer> awayFootballers = footballerDao.getByTeamId(footballers, awayTeamId);
            for (Footballer f : awayFootballers) {
                footballerDao.update(awayFootballers, f.getId(), buildOpponent(homeTeamId, awayTeamFixtureDifficulty));
            }

            playingFootballers.addAll(homeFootballers);
            playingFootballers.addAll(awayFootballers);
        }

        List<Footballer> nonPlayingFootballers = footballerDao.getInverse(footballers, playingFootballers);
        for (Footballer f : nonPlayingFootballers) {
            footballerDao.update(nonPlayingFootballers, f.getId(), buildOpponent(-1, 0));
        }
    }

    private Opponent buildOpponent(int oppositionId, int difficultyRating) throws NoFplResponseException {
        Opponent opponent = new Opponent();
        opponent.setDifficultyRating(difficultyRating);

        if (oppositionId == -1) {
            opponent.setName("NO FIXTURE");
            opponent.setTeamId(0);
        } else {
            opponent.setTeamId(oppositionId);
            List<Team> teamList = repo.getTeams();
            for (Team team : teamList) {
                if (oppositionId == team.getId()) {
                    opponent.setName(team.getShort_name());
                }
            }
        }
        return opponent;
    }
}
