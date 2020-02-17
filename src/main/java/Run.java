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
        DifficultyRating calculator = new DifficultyRating();
        List<Footballer> footballerList = calculator.getOpponentDifficulty(selection, week + 1, WEEKS_TO_EVALUATE);

        Collections.reverse(footballerList);
        for (Footballer footballer : footballerList) {
            System.out.println("Player: " + footballer.getWebName()
                    + "| Opponent: " + footballer.getOpponentList().toString()
                    + "| Total: " + footballer.getDifficultyTotal());
        }

        long endT = System.currentTimeMillis();
        System.out.println("Total: " + (endT - startT) + "ms");

    }
}
