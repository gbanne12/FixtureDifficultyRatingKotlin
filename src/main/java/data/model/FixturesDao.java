package data.model;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import exception.NoFplResponseException;
import fpl.FantasyPLService;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FixturesDao {

    public List<Fixture> getAllFixtures(int gameWeek) throws NoFplResponseException {
        try {
            FantasyPLService fplService = new FantasyPLService();
            JSONArray fixturesArray = fplService.getFixturesArray(gameWeek);

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
