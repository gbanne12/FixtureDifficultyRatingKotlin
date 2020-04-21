package data.model

import com.squareup.moshi.Moshi
import exception.NoFplResponseException
import fpl.url.FantasyEndpoint
import org.apache.commons.io.IOUtils
import org.json.JSONObject

import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets

class EventDao {

    /**
     * Get the currently active game week number
     * Will return the first unfinished game week, so an in-progress but unfinished week will be returned
     *
     * @return number of the first unfinished game week
     * @throws IOException if unable to get game week from the endpoint
     */
    val currentWeek: Int
        @Throws(NoFplResponseException::class)
        get() {
            val jsonBinder = Moshi.Builder().build()
            val eventAdapter = jsonBinder.adapter(Event::class.java)
            var week = 0
            try {
                val jsonString = IOUtils.toString(URL(FantasyEndpoint.STATIC.url), StandardCharsets.UTF_8)
                val eventArray = JSONObject(jsonString).getJSONArray("events")
                for (i in 0 until eventArray.length()) {
                    val event: Event? = eventAdapter.fromJson(eventArray.get(i).toString())
                    if (event != null && event.finished == false) {
                        week = event.id
                        break
                    }
                }
                //return week
                return 2
            } catch (exception: IOException) {
                throw NoFplResponseException(exception.message, exception)
            }
        }
}
