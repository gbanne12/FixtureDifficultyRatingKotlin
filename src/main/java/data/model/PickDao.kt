package data.model

import exception.NoFplResponseException
import fpl.url.FantasyEndpoint
import org.apache.commons.io.IOUtils
import org.json.JSONArray
import org.json.JSONObject

import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets

class PickDao {

    @Throws(NoFplResponseException::class)
    fun getPicks(teamId: Int, gameWeek: Int): JSONArray {
        try {
            val url = FantasyEndpoint.PICKS_PREFIX.url + teamId +
                    FantasyEndpoint.PICKS_INFIX.url+ gameWeek + FantasyEndpoint.PICKS_SUFFIX.url
            val jsonString = IOUtils.toString(URL(url), StandardCharsets.UTF_8)
            val json = JSONObject(jsonString)
            return json.getJSONArray("picks")
        } catch (e: IOException) {
            throw NoFplResponseException(e.message, e)
        }
    }
}
