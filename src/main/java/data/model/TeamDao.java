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

public class TeamDao {

    public List<Team> getTeams() throws NoFplResponseException {
        try {
            String staticUrl = IOUtils.toString(new URL(FantasyEndpoint.STATIC.url), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(staticUrl);
            JSONArray teamsArray = json.getJSONArray("teams");

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Team> teamsAdapter = moshi.adapter(Team.class);
            List<Team> teamList = new ArrayList<>();
            for (int j = 0; j < teamsArray.length(); j++) {
                teamList.add(teamsAdapter.fromJson(teamsArray.get(j).toString()));
            }
            return teamList;
        } catch (IOException e) {
            throw new NoFplResponseException(e.getMessage(), e);
        }
    }


}
