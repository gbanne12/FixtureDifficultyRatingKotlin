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

public class EventDao {

    /**
     * Get the currently active game week number
     * Will return the first unfinished game week, so an in-progress but unfinished week will be returned
     *
     * @return number of the first unfinished game week
     * @throws IOException
     */
    public int getCurrentWeek() throws NoFplResponseException {
        try {
            String staticUrl = IOUtils.toString(new URL(FantasyEndpoint.STATIC.url), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(staticUrl);
            JSONArray eventArray = json.getJSONArray("events");

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Event> eventAdapter = moshi.adapter(Event.class);
            int week = 0;
            for (int i = 0; i < eventArray.length(); i++) {
                Event event = eventAdapter.fromJson(eventArray.get(i).toString());
                if (event != null && event.finished.equals(false)) {
                    week = event.id;
                    break;
                }
            }
            return week;
        } catch (IOException e) {
            throw new NoFplResponseException(e.getMessage(), e);
        }
    }
}
