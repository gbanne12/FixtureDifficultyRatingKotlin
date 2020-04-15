package data.model;


import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FootballerDaoImpl implements FootballerDao {

    //List<Footballer> footballers = new ArrayList<>();

    public List<Footballer> getAll(int managerId, int gameWeek) throws IOException {
        JSONArray picks = new PicksDao().getPicks(managerId, gameWeek - 1);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Pick> picksAdapter = moshi.adapter(Pick.class);

        List<Footballer> footballers = new ArrayList<>();
        for (int i = 0; i < picks.length(); i++) {
            Pick pick = picksAdapter.fromJson((picks.get(i)).toString());
            if (pick != null) {
                Footballer footballer = new Footballer();
                footballer.setId(pick.element);
                footballer.setPosition(pick.position);
                footballers.add(footballer);
            }
        }
        return footballers;
    }

    public void update(List<Footballer> footballers, int id, Opponent opponent) {
        Footballer footballer = footballers.stream()
                .filter(x -> id == x.getId())
                .limit(1)
                .collect(Collectors.toList())
                .get(0);
        footballer.getOpponentList().add(opponent);
    }

    public List<Footballer> getInverse(List<Footballer> footballers, List<Footballer> toInvert) {
        Set<Integer> existingIds = toInvert
                .stream()
                .map(Footballer::getId)
                .collect(Collectors.toSet());

        return footballers
                .stream()
                .filter(footballer -> !existingIds.contains(footballer.getId()))
                .collect(Collectors.toList());
    }


    public List<Footballer> getByTeamId(List<Footballer> footballers, int teamId) {
        return footballers.stream()
                .filter(x -> teamId == x.getTeamId())
                .limit(3)
                .collect(Collectors.toList());
    }

}
