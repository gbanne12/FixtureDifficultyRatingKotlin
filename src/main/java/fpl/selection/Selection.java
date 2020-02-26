package fpl.selection;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import data.Repository;
import data.model.Element;
import data.model.Footballer;
import data.model.Pick;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Selection {

    private int teamId;
    private int week;
    private List<Footballer> footballerList;
    private Repository repo;

    public Selection(Repository repository) throws IOException {
        repo = repository;
        week = repo.getGameWeek();
    }

    public List<Footballer> getList(int teamId) throws IOException {
        if (this.teamId == teamId) {
            return footballerList;
        } else {
            this.teamId = teamId;
            JSONArray picks = repo.getPicks(teamId, week);
            footballerList = getPopulatedFootballerList(picks);
            return footballerList;
        }
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

    public List<Footballer> collectByTeamId(int teamId) {
        return footballerList.stream()
                .filter(x -> teamId == x.getTeamId())
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Footballer> collectInverse(List<Footballer> toInvert) {
        Set<Integer> existingIds = toInvert
                .stream()
                .map(Footballer::getId)
                .collect(Collectors.toSet());

        return footballerList
                .stream()
                .filter(footballer -> !existingIds.contains(footballer.getId()))
                .collect(Collectors.toList());
    }

}
