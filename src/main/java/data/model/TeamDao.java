package data.model;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import exception.NoFplResponseException;
import fpl.FantasyPLService;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamDao {

    public List<Team> getTeams() throws NoFplResponseException {
        try {


            FantasyPLService fplService = new FantasyPLService();
            JSONArray teamsArray = fplService.getTeamsArray();

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
