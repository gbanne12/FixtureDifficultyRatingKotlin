package fpl.event;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import fpl.FantasyPLService;
import json.Event;
import org.json.JSONArray;

import java.io.IOException;

public class GameWeek {
    /**
     * Get the currently active game week number
     * Will return the first unfinished game week, so an in-progress but unfinished week will be returned
     *
     * @return number of the first unfinished game week
     * @throws IOException
     */
    public int getCurrent() throws IOException {
        FantasyPLService fplService = new FantasyPLService();
        JSONArray events = fplService.getEventsArray();
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Event> eventAdapter = moshi.adapter(Event.class);
        int week = 0;
        for (int i = 0; i < events.length(); i++) {
            Event event = eventAdapter.fromJson(events.get(i).toString());
            if (event != null && event.finished.equals(false)) {
                week = event.id;
                break;
            }
        }
        return week;
    }
}
