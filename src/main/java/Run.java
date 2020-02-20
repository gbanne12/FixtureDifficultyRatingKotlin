import data.Repository;
import data.TransientRepository;
import fpl.score.DifficultyCalculator;
import fpl.teams.fantasy.Selection;
import model.Footballer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Run {

    private static final int WEEKS_TO_EVALUATE = 5;

    public static void main(String[] args) throws IOException {
        long startT = System.currentTimeMillis();

        Repository repository = new TransientRepository();
        Selection selection = new Selection(454545, repository);
        DifficultyCalculator calculator = new DifficultyCalculator(repository);
        List<Footballer> footballerList = calculator.difficultyRatingForWeeks(selection, WEEKS_TO_EVALUATE);

        Collections.sort(footballerList);
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
