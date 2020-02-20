package data.model;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import fpl.FantasyPLService;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FixturesDao {

    public List<Fixture> getAllFixtures(int gameWeek) throws IOException {
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
    }
}
