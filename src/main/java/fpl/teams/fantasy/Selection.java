package fpl.teams.fantasy;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import data.Repository;
import data.model.Element;
import data.model.Pick;
import model.Footballer;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Selection {

    private int teamId;
    private int week;
    private List<Footballer> footballerList;
    private Repository repo;

    public Selection(int teamId, Repository repository) throws IOException {
        repo = repository;
        week = repo.getGameWeek();
        JSONArray picks = repo.getPicks(week);
        footballerList = getPopulatedFootballerList(picks);
    }

    public List<Footballer> get() {
        return footballerList;
    }

    private List<Footballer> getPopulatedFootballerList(JSONArray picks) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Pick> picksAdapter = moshi.adapter(Pick.class);

        List<Footballer> footballerList = new ArrayList<>();
        for (int i = 0; i < picks.length(); i++) {
            Pick pick = picksAdapter.fromJson((picks.get(i)).toString());
            if (pick != null) {
                Footballer footballer = new Footballer();
                footballer.setId(pick.element);
                footballer.setPosition(pick.position);
                footballerList.add(footballer);
            }
        }

        // add team id and footballer name
        JSONArray elements = repo.getElements();
        JsonAdapter<Element> elementsAdapter = moshi.adapter(Element.class);

        for (int j = 0; j < elements.length(); j++) {
            Element element = elementsAdapter.fromJson((elements.get(j)).toString());
            for (Footballer footballer : footballerList) {
                if (element != null && element.id == footballer.getId()) {
                    footballer.setTeamId(element.team);
                    footballer.setWebName(element.web_name);
                }
            }
        }
        return footballerList;
    }

}
