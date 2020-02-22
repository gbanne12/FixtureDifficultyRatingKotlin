import data.Repository;
import data.TransientRepository;
import exception.NoFplResponseException;
import fpl.score.DifficultyCalculator;
import fpl.teams.fantasy.Selection;
import model.Footballer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ExampleRun {

    private static final int WEEKS_TO_EVALUATE = 5;
    private static int MANAGER_ID = 2029893;

    public static void main(String[] args) throws IOException {
        long startT = System.currentTimeMillis();

        try {
            Repository repository = new TransientRepository();
            Selection selection = new Selection(MANAGER_ID, repository);
            DifficultyCalculator calculator = new DifficultyCalculator(repository);
            List<Footballer> footballerList = calculator.difficultyRatingForWeeks(selection, WEEKS_TO_EVALUATE);

            Collections.sort(footballerList);
            Collections.reverse(footballerList);
            for (Footballer footballer : footballerList) {
                System.out.println("Player: " + footballer.getWebName()
                        + "| Opponent: " + footballer.getOpponentList().toString()
                        + "| Total: " + footballer.getDifficultyTotal());
            }

        } catch (NoFplResponseException e) {
            System.out.println("FAILED to get required FPL data: " + e.getMessage());
            // Handle failure
        }

        long endT = System.currentTimeMillis();
        System.out.println("Total: " + (endT - startT) + "ms");
    }
}
