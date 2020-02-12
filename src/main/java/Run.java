import fpl.event.GameWeek;
import model.Footballer;
import fpl.teams.fantasy.Selection;
import fpl.score.DifficultyRating;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Run {

    private static final int WEEKS_TO_EVALUATE = 5;

    public static void main(String[] args) throws IOException {
        int week = new GameWeek().getCurrent();
        Selection selection = new Selection(454545, week);
        List<Footballer> footballers = selection.get();

        for (int i = 0; i < WEEKS_TO_EVALUATE; i++) {
            System.out.println("Week " + i);
            DifficultyRating calculator = new DifficultyRating();
            footballers = calculator.getOpponentDifficulty(footballers, week + i);
        }

        Collections.sort(footballers);
        Collections.reverse(footballers);

        for (Footballer footballer : footballers) {
            System.out.println("Player: " + footballer.getWebName()
                            + "| Opponent: " + footballer.getOpponentList().toString()
                            + "| Total: " + footballer.getDifficultyTotal());
        }
    }
}
