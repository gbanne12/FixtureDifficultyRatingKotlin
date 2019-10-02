package service.fpl;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import url.FantasyEndpoint;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class FantasyPLService {

    public JSONArray getElementsArray() throws IOException {
        JSONObject elementsJson = getJsonObject(FantasyEndpoint.STATIC.url);
        return elementsJson.getJSONArray("elements");
    }

    public JSONArray getTeamsArray() throws IOException {
        JSONObject elementsJson = getJsonObject(FantasyEndpoint.STATIC.url);
        return elementsJson.getJSONArray("teams");
    }

    public JSONArray getFixturesArray(int gameWeek) throws IOException {
        URL fixturesEndpoint = new URL(FantasyEndpoint.FIXTURES.url + (gameWeek));
        return new JSONArray(IOUtils.toString(fixturesEndpoint, Charset.forName("UTF-8")));
    }

    public JSONArray getPicksArray(int gameWeek) throws IOException {
        JSONObject picksJson = getJsonObject(FantasyEndpoint.PICKS_PREFIX.url + gameWeek + FantasyEndpoint.PICKS_SUFFIX.url);
        return picksJson.getJSONArray("picks");
    }

    private JSONObject getJsonObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
    }
}
