package data.model;

import exception.NoFplResponseException;
import fpl.url.FantasyEndpoint;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PicksDao {

    public JSONArray getPicks(int teamId, int gameWeek) throws NoFplResponseException {
        try {
            String picksUrl = IOUtils.toString(
                    new URL(FantasyEndpoint.PICKS_PREFIX.url +
                            teamId +
                            FantasyEndpoint.PICKS_INFIX.url +
                            gameWeek +
                            FantasyEndpoint.PICKS_SUFFIX.url),
                    StandardCharsets.UTF_8);

            JSONObject json = new JSONObject(picksUrl);
            return json.getJSONArray("picks");
        } catch (IOException e) {
            throw new NoFplResponseException(e.getMessage(), e);
        }
    }
}
