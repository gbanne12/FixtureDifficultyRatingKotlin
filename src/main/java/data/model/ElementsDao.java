package data.model;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import exception.NoFplResponseException;
import fpl.url.FantasyEndpoint;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ElementsDao {

    public List<Element> getAll() throws NoFplResponseException {
        try {
            String staticJson = IOUtils.toString(new URL(FantasyEndpoint.STATIC.url), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(staticJson);
            JSONArray elementsArray = json.getJSONArray("elements");

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Element> elementsAdapter = moshi.adapter(Element.class);

            List<Element> elements = new ArrayList<>();
            for (int j = 0; j < elementsArray.length(); j++) {
                Element element = elementsAdapter.fromJson((elementsArray.get(j)).toString());
                elements.add(element);
            }
            return elements;
        } catch (IOException e) {
            throw new NoFplResponseException(e.getMessage(), e);
        }
    }
}
