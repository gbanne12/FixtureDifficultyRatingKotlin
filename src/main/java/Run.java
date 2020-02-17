import data.StaticRepository;
import fpl.score.DifficultyRating;
import fpl.teams.fantasy.Selection;
import model.Footballer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Run {

    private static final int WEEKS_TO_EVALUATE = 5;

    public static void main(String[] args) throws IOException {
        long startT = System.currentTimeMillis();
        int week = new StaticRepository().getCurrentWeek();
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
        long endT = System.currentTimeMillis();
        System.out.println("Total: " + (endT - startT) + "ms");

    }
}
