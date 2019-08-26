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

    public static void main(String[] args) throws IOException {
        Run run = new Run();
        List<Footballer> footballers = run.getFootballersInSquad();
        run.setFootballerNameAndTeamId(footballers);
        run.setTeamName(footballers);
        run.setFixtureDifficultyRating(footballers);

        for (Footballer footballer : footballers) {
            System.out.println("Player: " + footballer.getWebName() +
                    "| Opposition: " + footballer.getOppositionList().toString() +
                    "| Total: " + footballer.getDifficultyTotal());
        }
    }

    private JSONObject getJsonObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
    }

    private List<Footballer> getFootballersInSquad() throws IOException {
        JSONObject picksJson = getJsonObject(JsonUrl.PICKS_PREFIX.url + CURRENT_WEEK + JsonUrl.PICKS_SUFFIX.url);
        JSONArray picksArray = picksJson.getJSONArray("picks");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Pick> picksAdapter = moshi.adapter(Pick.class);

        List<Footballer> footballers = new ArrayList<>();
        for (int i = 0; i < picksArray.length(); i++) {
            Pick pick = picksAdapter.fromJson((picksArray.get(i)).toString());
            if (pick != null) {
                Footballer footballer = new Footballer();
                footballer.setId(pick.element);
                footballer.setPosition(pick.position);
                footballers.add(footballer);
            }
        }
        return footballers;
    }

    private void setFootballerNameAndTeamId(List<Footballer> footballers) throws IOException {
        JSONObject elementsJson = getJsonObject(JsonUrl.STATIC.url);
        JSONArray elementsArray = elementsJson.getJSONArray("elements");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Element> elementsAdapter = moshi.adapter(Element.class);

        for (int j = 0; j < elementsArray.length(); j++) {
            Element element = elementsAdapter.fromJson((elementsArray.get(j)).toString());
            for (Footballer footballer : footballers) {
                if (element != null && element.id == footballer.getId()) {
                    footballer.setTeamId(element.team);
                    footballer.setWebName(element.web_name);
                }
            }
        }
    }

    private void setTeamName(List<Footballer> footballers) throws IOException {
        JSONObject elementsJson = getJsonObject(JsonUrl.STATIC.url);
        JSONArray teamsArray = elementsJson.getJSONArray("teams");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Team> teamsAdapter = moshi.adapter(Team.class);
        for (int i = 0; i < teamsArray.length(); i++) {
            Team team = teamsAdapter.fromJson(teamsArray.get(i).toString());
            for (Footballer footballer : footballers) {
                if (team != null && team.id == footballer.getTeamId()) {
                    footballer.setTeamName(team.short_name);
                }
            }
        }
    }

    private void setFixtureDifficultyRating(List<Footballer> footballers) throws IOException {
        //  Get the fixtures for each
        for (int weekCounter = 1; weekCounter < WEEKS_TO_EVALUATE; weekCounter++) {
            JSONArray fixturesArray = new JSONArray(IOUtils.toString(
                    new URL(JsonUrl.FIXTURES.url + (CURRENT_WEEK + weekCounter)), Charset.forName("UTF-8")));
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Fixture> fixturesAdapter = moshi.adapter(Fixture.class);

            for (int i = 0; i < fixturesArray.length(); i++) {
                Fixture fixture = fixturesAdapter.fromJson(fixturesArray.get(i).toString());
                for (Footballer footballer : footballers) {
                    assert fixture != null;
                    boolean isFootballerPartOfAwayTeam = (fixture.team_a == footballer.getTeamId());
                    boolean isFootballerPartOfHomeTeam = (fixture.team_h == footballer.getTeamId());

                    if (isFootballerPartOfAwayTeam) {
                        setOppositionNameAndDifficulty(footballer, fixture, false);
                    } else if (isFootballerPartOfHomeTeam) {
                        setOppositionNameAndDifficulty(footballer, fixture, true);
                    }
                }
            }
        }
    }

    private void setOppositionNameAndDifficulty(Footballer footballer, Fixture fixture, boolean isFootballerAtHome) throws IOException {
        Opposition opposition = new Opposition();
        if (isFootballerAtHome) {
            opposition.setTeamId(fixture.team_a);
            opposition.setDifficultyRating(fixture.team_h_difficulty);
        } else {
            opposition.setTeamId(fixture.team_h);
            opposition.setDifficultyRating(fixture.team_a_difficulty);
        }

        //  Translate the team id to text
        JSONObject elementsJson = getJsonObject(JsonUrl.STATIC.url);
        JSONArray teamsArray = elementsJson.getJSONArray("teams");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Team> teamsAdapter = moshi.adapter(Team.class);
        for (int j = 0; j < teamsArray.length(); j++) {
            Team team = teamsAdapter.fromJson(teamsArray.get(j).toString());
            if (team != null && team.id == opposition.getTeamId()) {
                opposition.setName(team.name);
            }
        }

        List<Opposition> oppositionList = footballer.getOppositionList();
        oppositionList.add(opposition);
        footballer.setOppositionList(oppositionList);
        int currentDifficultyScore = footballer.getDifficultyTotal();
        footballer.setDifficultyTotal(currentDifficultyScore + opposition.getDifficultyRating());
    }

}
