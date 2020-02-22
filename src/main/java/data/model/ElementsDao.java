package data.model;

import exception.NoFplResponseException;
import fpl.FantasyPLService;
import org.json.JSONArray;

import java.io.IOException;

public class ElementsDao {

    public JSONArray getElements() throws NoFplResponseException {
        try {
            FantasyPLService fplService = new FantasyPLService();
            return fplService.getElementsArray();
        } catch (IOException e) {
            throw new NoFplResponseException(e.getMessage(), e);
        }
    }
}
