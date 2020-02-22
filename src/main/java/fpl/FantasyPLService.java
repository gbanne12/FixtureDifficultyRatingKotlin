package fpl;

import fpl.url.FantasyEndpoint;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class FantasyPLService {

    /**
     * @return events array detailing the status of the 38 game weeks
     * @throws IOException
     */
    public JSONArray getEventsArray() throws IOException {
        JSONObject elementsJson = getJsonObject(FantasyEndpoint.STATIC.url);
        return elementsJson.getJSONArray("events");
    }

    /**
     * @return elements array detailing the status of the footballers for selection
     * @throws IOException
     */
    public JSONArray getElementsArray() throws IOException {
        JSONObject elementsJson = getJsonObject(FantasyEndpoint.STATIC.url);
        return elementsJson.getJSONArray("elements");
    }

    /**
     * @return teams array detailing the 20 premier league teams
     * @throws IOException
     */
    public JSONArray getTeamsArray() throws IOException {
        JSONObject elementsJson = getJsonObject(FantasyEndpoint.STATIC.url);
        return elementsJson.getJSONArray("teams");
    }

    /**
     * @param week the game week fixtures to return
     * @return fixtures array detailing the fixtures to be played in that week
     * @throws IOException
     */
    public JSONArray getFixturesArray(int week) throws IOException {
        URL fixturesEndpoint = new URL(FantasyEndpoint.FIXTURES.url + (week));
        return new JSONArray(IOUtils.toString(fixturesEndpoint, Charset.forName("UTF-8")));
    }

    /**
     * @param week the game week to return selections for
     * @return picks array detailing the footballers selected for a manager id
     * @throws IOException
     */
    public JSONArray getPicksArray(int teamId, int week) throws IOException {
        JSONObject picksJson = getJsonObject(
                FantasyEndpoint.PICKS_PREFIX.url + teamId + FantasyEndpoint.PICKS_INFIX.url + week + FantasyEndpoint.PICKS_SUFFIX.url);
        return picksJson.getJSONArray("picks");
    }

    private JSONObject getJsonObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
    }
}
