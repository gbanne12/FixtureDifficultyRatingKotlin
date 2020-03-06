import data.Repository;
import data.TransientRepository;
import data.model.Footballer;
import exception.NoFplResponseException;
import fpl.score.FixtureCalculator;

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
            FixtureCalculator calculator = new FixtureCalculator(repository);
            List<Footballer> footballers = calculator.getFixturesDifficulty(MANAGER_ID, WEEKS_TO_EVALUATE);

            Collections.sort(footballers);
            Collections.reverse(footballers);
            for (Footballer footballer : footballers) {
                System.out.println("Player: " + footballer.getWebName() +
                                "| Opponent: " + footballer.getOpponentList().toString() +
                                "| Total: " + footballer.getDifficultyTotal());
            }

        } catch (NoFplResponseException e) {
            System.out.println("FAILED to get required FPL data: " + e.getMessage());
            // Handle failure
        }

        long endT = System.currentTimeMillis();
        System.out.println("Total: " + (endT - startT) + "ms");
    }
}
