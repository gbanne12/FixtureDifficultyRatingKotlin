package fpl.score;

import data.Repository;
import data.model.Footballer;
import data.model.Opponent;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class FixtureCalculatorTest {

    private List<Footballer> footballers;

    @Before
    public void setup() throws IOException {
        Repository mockRepo = new MockRepository();
        FixtureCalculator calculator = new FixtureCalculator(mockRepo);
        footballers = calculator.getFixturesDifficulty(0, 1);
    }

    @Test
    public void canGetMultipleFootballers() {
        assertEquals(7, footballers.size());
    }

    @Test
    public void canGetFootballerIds() {
        assertEquals(1, footballers.get(0).getId());
        assertEquals(2, footballers.get(1).getId());
    }

    @Test
    public void canGetFootballerPosition() {
        assertEquals(1, footballers.get(0).getPosition());
        assertEquals(2, footballers.get(1).getPosition());
    }

    @Test
    public void canGetTeamIds() {
        int teamForFootballerOneAndTwo = 2;
        int teamForFootballerThree = 3;

        assertEquals(teamForFootballerOneAndTwo, footballers.get(0).getTeamId());
        assertEquals(teamForFootballerOneAndTwo, footballers.get(1).getTeamId());
        assertEquals(teamForFootballerThree, footballers.get(2).getTeamId());
    }

    @Test
    public void canGetWebName() {
        Footballer one = footballers.get(0);
        Footballer two = footballers.get(1);
        String oneExpectedName = "Element One";
        String twoExpectedName = "Element Two";

        assertEquals(oneExpectedName, one.getWebName());
        assertEquals(twoExpectedName, two.getWebName());
    }

    @Test
    public void canGetOppositionNameForHomeFootballer() {
        // Footballer one and two are in team 2 with 2 fixtures totalling a difficulty of 6
        Footballer one = footballers.get(0);

        Opponent firstOpponentForTeam2 = new Opponent();
        firstOpponentForTeam2.setName("Three");
        firstOpponentForTeam2.setTeamId(3);
        firstOpponentForTeam2.setDifficultyRating(5);

        Opponent secondOpponentForTeam2 = new Opponent();
        secondOpponentForTeam2.setName("Six");
        secondOpponentForTeam2.setTeamId(6);
        secondOpponentForTeam2.setDifficultyRating(1);

        assertTrue(one.getOpponentList().contains(firstOpponentForTeam2));
        assertTrue(one.getOpponentList().contains(secondOpponentForTeam2));
    }

    @Test
    public void canGetOppositionNameForAwayFootballer() {
        Footballer three = footballers.get(2);

        Opponent opponentForTeam3 = new Opponent();
        opponentForTeam3.setName("Two");
        opponentForTeam3.setTeamId(2);
        opponentForTeam3.setDifficultyRating(3);

        assertTrue(three.getOpponentList().contains(opponentForTeam3));
    }

    @Test
    public void canGetOppositionNameForFootballerWithNoFixture() {
        Footballer six = footballers.get(5);

        Opponent opponentForTeam4 = new Opponent();
        opponentForTeam4.setName("NO FIXTURE");
        opponentForTeam4.setTeamId(0);
        opponentForTeam4.setDifficultyRating(0);

        assertTrue(six.getOpponentList().contains(opponentForTeam4));
    }


    @Test
    public void canGetDifficultyForHomeFootballers() {
        // Footballer one and two are in team 2 with 2 fixtures totalling a difficulty of 6
        Footballer one = footballers.get(0);
        Footballer two = footballers.get(1);
        int expectedDifficultyForTeam2 = 6;

        // Footballer five is in team 20 with a difficulty of 2
        Footballer five = footballers.get(4);
        int expectedDifficultyForTeam20 = 2;

        assertEquals(expectedDifficultyForTeam2, one.getDifficultyTotal());
        assertEquals(expectedDifficultyForTeam2, two.getDifficultyTotal());
        assertEquals(expectedDifficultyForTeam20, five.getDifficultyTotal());
    }

    @Test
    public void canGetDifficultyForAwayFootballers() {
        // Footballer three is in team 3 with a difficulty of 3
        Footballer three = footballers.get(2);
        int expectedDifficultyForTeam3 = 3;

        // Footballer four is in team 5 with a difficulty of 4
        Footballer four = footballers.get(3);
        int expectedDifficultyForTeam5 = 4;

        Footballer seven = footballers.get(6);
        int expectedDifficultyForTeam6 = 2;

        assertEquals(expectedDifficultyForTeam3, three.getDifficultyTotal());
        assertEquals(expectedDifficultyForTeam5, four.getDifficultyTotal());
        assertEquals(expectedDifficultyForTeam6, seven.getDifficultyTotal());
    }

    @Test
    public void canGetZeroDifficultyForFootballerWithNoFixture() {
        Footballer six = footballers.get(5);
        int expectedDifficulty = 0;
        assertEquals(expectedDifficulty, six.getDifficultyTotal());
    }

}
