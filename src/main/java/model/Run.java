package model;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import model.json.Element;
import model.json.Fixture;
import model.json.Pick;
import model.json.Team;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import url.JsonUrl;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Run {

    private static final int CURRENT_WEEK = 2;
    private static final int WEEKS_TO_EVALUATE = 5;

    public static void main(String args[]) throws IOException {
        Run run = new Run();

        // Get a list of currently selected players
        JSONObject picksJson = run.getJsonObject(JsonUrl.PICKS_PREFIX.url + CURRENT_WEEK + JsonUrl.PICKS_SUFFIX.url);
        JSONArray picksArray = picksJson.getJSONArray("picks");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Pick> picksAdapter = moshi.adapter(Pick.class);

        List<Footballer> footballers = new ArrayList<>();
        for (int i = 0; i < picksArray.length(); i++) {
            Pick pick = picksAdapter.fromJson((picksArray.get(i)).toString());
            Footballer footballer = new Footballer();
            footballer.setId(pick.element);
            footballer.setPosition(pick.position);
            footballers.add(footballer);
            System.out.println(pick.element);
        }

        // For the selected player ids get the footballer name and team id
        JSONObject elementsJson = run.getJsonObject(JsonUrl.STATIC.url);
        JSONArray elementsArray = elementsJson.getJSONArray("elements");
        JsonAdapter<Element> elementsAdapter = moshi.adapter(Element.class);

        for (int j = 0; j < elementsArray.length(); j++) {
            Element element = elementsAdapter.fromJson((elementsArray.get(j)).toString());
            for (Footballer footballer : footballers) {
                if (element.id == footballer.getId()) {
                    footballer.setTeamId(element.team);
                    footballer.setWebName(element.web_name);
                }
            }
        }

        // Get the text value for the team id
        JSONArray teamsArray = elementsJson.getJSONArray("teams");
        JsonAdapter<Team> teamsAdapter = moshi.adapter(Team.class);
        for (int i = 0; i < teamsArray.length(); i++) {
            Team team = teamsAdapter.fromJson(teamsArray.get(i).toString());
            for (Footballer footballer : footballers) {
                if (team.id == footballer.getTeamId()) {
                    footballer.setTeamName(team.short_name);
                }
            }
        }

        //  Get the fixtures for each
        for (int weekCounter = 1; weekCounter < WEEKS_TO_EVALUATE; weekCounter++) {


            JSONArray fixturesArray = new JSONArray(IOUtils.toString(
                    new URL(JsonUrl.FIXTURES.url + (CURRENT_WEEK + weekCounter)), Charset.forName("UTF-8")));
            JsonAdapter<Fixture> fixturesAdapter = moshi.adapter(Fixture.class);
            for (int i = 0; i < fixturesArray.length(); i++) {
                Fixture fixture = fixturesAdapter.fromJson(fixturesArray.get(i).toString());
                for (Footballer footballer : footballers) {
                    if (fixture.team_a == footballer.getTeamId()) {
                        Opposition opposition = new Opposition();
                        opposition.setTeamId(fixture.team_h);
                        opposition.setDifficultyRating(fixture.team_h_difficulty);
                        List<Opposition> oppositionList = footballer.getOppositionList();
                        oppositionList.add(opposition);
                        footballer.setOppositionList(oppositionList);
                        int currentDifficultyScore = footballer.getDifficultyTotal();
                        footballer.setDifficultyTotal(currentDifficultyScore + opposition.getDifficultyRating());
                    } else if (fixture.team_h == footballer.getTeamId()) {
                        Opposition opposition = new Opposition();
                        opposition.setTeamId(fixture.team_a);
                        opposition.setDifficultyRating(fixture.team_a_difficulty);
                        List<Opposition> oppositionList = footballer.getOppositionList();
                        oppositionList.add(opposition);
                        footballer.setOppositionList(oppositionList);
                        int currentDifficultyScore = footballer.getDifficultyTotal();
                        footballer.setDifficultyTotal(currentDifficultyScore + opposition.getDifficultyRating());
                    }
                }

            }
        }

        for (Footballer footballer: footballers) {
            System.out.println("Player: " + footballer.getWebName() + " | Opposition: " + footballer.getOppositionList().toString()
            + "| Total: " + footballer.getDifficultyTotal());
        }

    }

    public JSONObject getJsonObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
    }
}
