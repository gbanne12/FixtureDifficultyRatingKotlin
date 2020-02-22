package fpl.score;

import data.Repository;
import data.model.Fixture;
import data.model.Footballer;
import data.model.Opponent;
import data.model.Team;
import exception.NoFplResponseException;
import fpl.selection.Selection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DifficultyCalculator {
    private List<Team> teamList;
    private Repository repo;

    public DifficultyCalculator(Repository repository) {
        repo = repository;
    }

    public List<Footballer> difficultyRatingForWeeks(Selection selection, int weeksToCalculate) throws IOException {
        List<Footballer> footballers = selection.get();
        for (int i = 0; i < weeksToCalculate; i++) {
            footballers = difficultyRating(selection, repo.getGameWeek() + i);
        }
        return footballers;
    }

    public List<Footballer> difficultyRating(Selection selection, int gameWeek) throws IOException {
        List<Footballer> footballers = selection.get();
        List<Fixture> fixtures = repo.getFixtures(gameWeek);
        List<Footballer> footballersWithFixture = new ArrayList<>();

        for (Fixture fixture : fixtures) {
            int homeTeamId = fixture.team_h;
            int homeTeamFixtureDifficulty = fixture.team_h_difficulty;
            int awayTeamId = fixture.team_a;
            int awayTeamFixtureDifficulty = fixture.team_a_difficulty;

            List<Footballer> homeFootballers = collectByTeamId(footballers, homeTeamId);
            addOpponent(homeFootballers, awayTeamId, homeTeamFixtureDifficulty);

            List<Footballer> awayFootballers = collectByTeamId(footballers, awayTeamId);
            addOpponent(awayFootballers, homeTeamId, awayTeamFixtureDifficulty);

            footballersWithFixture.addAll(homeFootballers);
            footballersWithFixture.addAll(awayFootballers);
        }

        List<Footballer> footballersWithNoFixture = collectWithoutFixture(footballers, footballersWithFixture);
        addEmptyOpponent(footballersWithNoFixture);
        return footballers;
    }

    private List<Footballer> collectByTeamId(List<Footballer> footballers, int teamId) {
        return footballers.stream()
                .filter(x -> teamId == x.getTeamId())
                .limit(3)
                .collect(Collectors.toList());
    }

    private List<Footballer> collectWithoutFixture(List<Footballer> footballers, List<Footballer> withFixture) {
        Set<Integer> footballersWithFixtureIds = withFixture
                .stream()
                .map(Footballer::getId)
                .collect(Collectors.toSet());

        return footballers
                .stream()
                .filter(footballer -> !footballersWithFixtureIds.contains(footballer.getId()))
                .collect(Collectors.toList());
    }

    private void addOpponent(List<Footballer> footballers, int opponentId, int rating) throws NoFplResponseException {
        for (Footballer footballer : footballers) {
            Opponent opponent = new Opponent();
            opponent.setTeamId(opponentId);
            opponent.setDifficultyRating(rating);
            teamList = repo.getTeams();
            for (Team team : teamList) {
                if (opponentId == team.id) {
                    opponent.setName(team.short_name);
                }
            }
            footballer.getOpponentList().add(opponent);
        }
    }

    private void addEmptyOpponent(List<Footballer> footballers) {
        for (Footballer footballer : footballers) {
            Opponent opponent = new Opponent();
            opponent.setName("NOT PLAYING");
            opponent.setDifficultyRating(0);
            List<Opponent> opponentList = footballer.getOpponentList();
            opponentList.add(opponent);
            footballer.setOpponentList(opponentList);
        }
    }
}
