package fpl.score

import data.model.Footballer
import data.model.Opponent
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class FixtureCalculatorTest {

    private var footballers: List<Footballer>? = null

    @Before
    @Throws(IOException::class)
    fun setup() {
        val mockRepo = MockRepository()
        val calculator = FixtureCalculator(mockRepo)
        footballers = calculator.getFixturesDifficulty(0, 1)
    }

    @Test
    fun canGetMultipleFootballers() {
        assertEquals(7, footballers!!.size.toLong())
    }

    @Test
    fun canGetFootballerIds() {
        assertEquals(1, footballers!![0].id.toLong())
        assertEquals(2, footballers!![1].id.toLong())
    }

    @Test
    fun canGetFootballerPosition() {
        assertEquals(1, footballers!![0].position.toLong())
        assertEquals(2, footballers!![1].position.toLong())
    }

    @Test
    fun canGetTeamIds() {
        val teamForFootballerOneAndTwo = 2
        val teamForFootballerThree = 3

        assertEquals(teamForFootballerOneAndTwo.toLong(), footballers!![0].teamId.toLong())
        assertEquals(teamForFootballerOneAndTwo.toLong(), footballers!![1].teamId.toLong())
        assertEquals(teamForFootballerThree.toLong(), footballers!![2].teamId.toLong())
    }

    @Test
    fun canGetWebName() {
        val one = footballers!![0]
        val two = footballers!![1]
        val oneExpectedName = "Element One"
        val twoExpectedName = "Element Two"

        assertEquals(oneExpectedName, one.webName)
        assertEquals(twoExpectedName, two.webName)
    }

    @Test
    fun canGetOppositionNameForHomeFootballer() {
        // Footballer one and two are in team 2 with 2 fixtures totalling a difficulty of 6
        val one = footballers!![0]

        val firstOpponentForTeam2 = Opponent()
        firstOpponentForTeam2.name = "Three"
        firstOpponentForTeam2.teamId = 3
        firstOpponentForTeam2.difficultyRating = 5

        val secondOpponentForTeam2 = Opponent()
        secondOpponentForTeam2.name = "Six"
        secondOpponentForTeam2.teamId = 6
        secondOpponentForTeam2.difficultyRating = 1

        assertTrue(one.opponentList.contains(firstOpponentForTeam2))
        assertTrue(one.opponentList.contains(secondOpponentForTeam2))
    }

    @Test
    fun canGetOppositionNameForAwayFootballer() {
        val three = footballers!![2]

        val opponentForTeam3 = Opponent()
        opponentForTeam3.name = "Two"
        opponentForTeam3.teamId = 2
        opponentForTeam3.difficultyRating = 3

        assertTrue(three.opponentList.contains(opponentForTeam3))
    }

    @Test
    fun canGetOppositionNameForFootballerWithNoFixture() {
        val six = footballers!![5]

        val opponentForTeam4 = Opponent()
        opponentForTeam4.name = "NO FIXTURE"
        opponentForTeam4.teamId = 0
        opponentForTeam4.difficultyRating = 0

        assertTrue(six.opponentList.contains(opponentForTeam4))
    }


    @Test
    fun canGetDifficultyForHomeFootballers() {
        // Footballer one and two are in team 2 with 2 fixtures totalling a difficulty of 6
        val one = footballers!![0]
        val two = footballers!![1]
        val expectedDifficultyForTeam2 = 6

        // Footballer five is in team 20 with a difficulty of 2
        val five = footballers!![4]
        val expectedDifficultyForTeam20 = 2

        assertEquals(expectedDifficultyForTeam2.toLong(), one.difficultyTotal.toLong())
        assertEquals(expectedDifficultyForTeam2.toLong(), two.difficultyTotal.toLong())
        assertEquals(expectedDifficultyForTeam20.toLong(), five.difficultyTotal.toLong())
    }

    @Test
    fun canGetDifficultyForAwayFootballers() {
        // Footballer three is in team 3 with a difficulty of 3
        val three = footballers!![2]
        val expectedDifficultyForTeam3 = 3

        // Footballer four is in team 5 with a difficulty of 4
        val four = footballers!![3]
        val expectedDifficultyForTeam5 = 4

        val seven = footballers!![6]
        val expectedDifficultyForTeam6 = 2

        assertEquals(expectedDifficultyForTeam3.toLong(), three.difficultyTotal.toLong())
        assertEquals(expectedDifficultyForTeam5.toLong(), four.difficultyTotal.toLong())
        assertEquals(expectedDifficultyForTeam6.toLong(), seven.difficultyTotal.toLong())
    }

    @Test
    fun canGetZeroDifficultyForFootballerWithNoFixture() {
        val six = footballers!![5]
        val expectedDifficulty = 0
        assertEquals(expectedDifficulty.toLong(), six.difficultyTotal.toLong())
    }

}
