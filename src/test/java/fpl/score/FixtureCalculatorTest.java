package fpl.score;

import data.model.Footballer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FixtureCalculatorTest {

    private List<Footballer> footballers;

    @Before
    public void setup() throws IOException {
        FixtureCalculator calculator = new FixtureCalculator(new MockRepository());
        footballers = calculator.getFixturesDifficulty(0, 1);
    }


    @Test
    public void canGetMultipleFootballers() {
        assertEquals(5, footballers.size());
    }
    
}
