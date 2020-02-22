package data.model;

import exception.NoFplResponseException;
import fpl.FantasyPLService;
import org.json.JSONArray;

import java.io.IOException;

public class PicksDao {

    public JSONArray getPicks(int teamId, int gameWeek) throws NoFplResponseException {
        try {
            FantasyPLService fplService = new FantasyPLService();
            return fplService.getPicksArray(teamId, gameWeek);
        } catch (IOException e) {
            throw new NoFplResponseException(e.getMessage(), e);
        }
    }
}
