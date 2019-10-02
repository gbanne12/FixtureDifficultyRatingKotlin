package model;

import squad.Squad;
import squad.score.FixtureDifficultyCalculator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Run {

    private static final int GAMEWEEK_FOR_TEAM_SELECTION = 7;
    private static final int WEEKS_TO_EVALUATE = 5;

    public static void main(String[] args) throws IOException {
        Squad squad = new Squad();
        List<Footballer> footballers = squad.get(GAMEWEEK_FOR_TEAM_SELECTION);

        FixtureDifficultyCalculator calculator = new FixtureDifficultyCalculator();
        footballers = calculator.getDifficulty(footballers, GAMEWEEK_FOR_TEAM_SELECTION, WEEKS_TO_EVALUATE);

        Collections.sort(footballers);
        Collections.reverse(footballers);

        for (Footballer footballer : footballers) {
            System.out.println("Player: " + footballer.getWebName() +
                    "| Opponent: " + footballer.getOpponentList().toString() +
                    "| Total: " + footballer.getDifficultyTotal());
        }
    }
}
