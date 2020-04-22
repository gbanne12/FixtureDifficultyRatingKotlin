import data.TransientRepository
import data.model.Footballer
import exception.NoFplResponseException
import fpl.score.FixtureCalculator
import java.io.IOException

object ExampleRun {

    private const val WEEKS_TO_EVALUATE = 5
    private const val MANAGER_ID = 2029893

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val startTime = System.currentTimeMillis()
        try {
            val repository = TransientRepository()
            val calculator = FixtureCalculator(repository)
            val footballers: MutableList<Footballer> = calculator.getFixturesDifficulty(MANAGER_ID, WEEKS_TO_EVALUATE)

            footballers.sortByDescending { footballer -> footballer.difficultyTotal }
            for (footballer in footballers) {
                val name = footballer.webName
                val opponentString = footballer.opponentList.toString()
                val total = footballer.difficultyTotal
                println("Player: $name | Opponent: $opponentString | Total: $total")
            }
        } catch (e: NoFplResponseException) {
            println("FAILED to get required FPL data: " + e.message)
            // Handle failure
        }

        val endTime = System.currentTimeMillis()
        println("Total: " + (endTime - startTime) + "ms")
    }
}
