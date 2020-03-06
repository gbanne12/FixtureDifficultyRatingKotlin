package data.model;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import exception.NoFplResponseException;
import fpl.url.FantasyEndpoint;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FixturesDao {

    public List<Fixture> getAll(int gameWeek) throws NoFplResponseException {
        try {
            String url = IOUtils.toString(new URL(FantasyEndpoint.FIXTURES.url + (gameWeek)), StandardCharsets.UTF_8);
            JSONArray fixturesArray = new JSONArray(url);
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Fixture> fixturesAdapter = moshi.adapter(Fixture.class);
            List<Fixture> fixtures = new ArrayList<>();
            for (int i = 0; i < fixturesArray.length(); i++) {
                Fixture fixture = fixturesAdapter.fromJson(fixturesArray.get(i).toString());
                fixtures.add(fixture);
            }
            return fixtures;

        } catch (IOException e) {
            throw new NoFplResponseException(e.getMessage(), e);
        }
    }
}
