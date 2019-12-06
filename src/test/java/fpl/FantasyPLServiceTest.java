package fpl;

import org.json.JSONArray;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.*;

public class FantasyPLServiceTest {

    @Test
    public void getPicksArrayTest() throws IOException {
        FantasyPLService fplService = new FantasyPLService();
        JSONArray array = fplService.getPicksArray(1);
        assertNotNull(array);
        assertEquals("Should have 15 selected picks", 15, array.length());
    }

    @Test
    public void getElementsArrayTest() throws IOException {
        FantasyPLService fplService = new FantasyPLService();
        JSONArray array = fplService.getElementsArray();
        assertNotNull(array);
        assertTrue("Should have some data", array.length() > 0);
    }

    @Test
    public void getTeamsArrayTest() throws IOException {
        FantasyPLService fplService = new FantasyPLService();
        JSONArray array = fplService.getTeamsArray();
        assertEquals("Should always be 20 teams", 20, array.length());
    }

    @Test
    public void getFixturesArrayTest() throws IOException {
        FantasyPLService fplService = new FantasyPLService();
        JSONArray array = fplService.getFixturesArray(1);
        assertEquals("Should always be 10 fixtures", 10, array.length());
    }
}
