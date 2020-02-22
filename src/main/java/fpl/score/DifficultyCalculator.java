package fpl.score;

import data.Repository;
import data.model.Fixture;
import data.model.Team;
import fpl.selection.Selection;
import model.Footballer;
import model.Opponent;

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
        int currentWeek = repo.getGameWeek();
        for (int i = 0; i < weeksToCalculate; i++) {
            footballers = difficultyRating(selection, currentWeek + i);
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


            List<Footballer> homeFootballers = filterByTeamId(footballers, homeTeamId);
            for (Footballer footballer : homeFootballers) {
                footballer.setOpponentList(getOpponentList(footballer, awayTeamId, homeTeamFixtureDifficulty));
            }
            footballersWithFixture.addAll(homeFootballers);

            List<Footballer> awayFootballers = filterByTeamId(footballers, awayTeamId);
            for (Footballer footballer : awayFootballers) {
                footballer.setOpponentList(getOpponentList(footballer, homeTeamId, awayTeamFixtureDifficulty));
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

    private List<Footballer> filterByTeamId(List<Footballer> footballers, int teamId) {
        return footballers.stream()
                .filter(x -> teamId == x.getTeamId())
                .limit(3)
                .collect(Collectors.toList());
    }

    private List<Opponent> getOpponentList(Footballer footballer, int opponentId, int rating) throws IOException {
        Opponent opponent = new Opponent();
        opponent.setTeamId(opponentId);
        teamList = repo.getTeams();
        for (Team team : teamList) {
            if (opponentId == team.id) {
                opponent.setName(team.short_name);
            }
        }
        List<Opponent> opponentList = footballer.getOpponentList();
        opponent.setDifficultyRating(rating);
        opponentList.add(opponent);
        footballer.setOpponentList(opponentList);
        return opponentList;
    }
}
