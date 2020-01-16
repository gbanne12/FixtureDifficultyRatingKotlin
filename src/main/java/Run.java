import fpl.event.GameWeek;
import model.Footballer;
import fpl.teams.fantasy.Squad;
import fpl.score.FixtureDifficultyCalculator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Run {

    private static final int WEEKS_TO_EVALUATE = 5;

    public static void main(String[] args) throws IOException {
        GameWeek gameWeek = new GameWeek();
        int week = gameWeek.getCurrent();

        Squad squad = new Squad();
        List<Footballer> footballers = squad.get(week - 1);

        FixtureDifficultyCalculator calculator = new FixtureDifficultyCalculator();
        footballers = calculator.getDifficultyTotal(footballers, week, WEEKS_TO_EVALUATE);

        Collections.sort(footballers);
        Collections.reverse(footballers);

        for (Footballer footballer : footballers) {
            System.out.println("Player: " + footballer.getWebName()
                            + "| Opponent: " + footballer.getOpponentList().toString()
                            + "| Total: " + footballer.getDifficultyTotal());
        }
    }
}