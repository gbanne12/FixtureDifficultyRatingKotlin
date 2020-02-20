package data.model;

import fpl.FantasyPLService;
import org.json.JSONArray;

import java.io.IOException;

public class ElementsDao {

    public JSONArray getElements() throws IOException {
        FantasyPLService fplService = new FantasyPLService();
        return fplService.getElementsArray();
    }
}
