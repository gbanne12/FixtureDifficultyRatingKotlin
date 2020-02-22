package data.model;

import exception.NoFplResponseException;
import fpl.url.FantasyEndpoint;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ElementsDao {

    public JSONArray getElements() throws NoFplResponseException {
        try {
            String staticUrl = IOUtils.toString(new URL(FantasyEndpoint.STATIC.url), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(staticUrl);
            return json.getJSONArray("elements");
        } catch (IOException e) {
            throw new NoFplResponseException(e.getMessage(), e);
        }
    }
}
