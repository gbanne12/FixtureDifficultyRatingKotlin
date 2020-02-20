package data.model;

import fpl.FantasyPLService;
import org.json.JSONArray;

import java.io.IOException;

public class PicksDao {

    public JSONArray getPicks(int teamId, int gameWeek) throws IOException {
        FantasyPLService fplService = new FantasyPLService();
        return fplService.getPicksArray(teamId, gameWeek);
    }
}
