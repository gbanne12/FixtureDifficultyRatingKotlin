package data.model

import com.squareup.moshi.Moshi
import exception.NoFplResponseException
import fpl.url.FantasyEndpoint
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

class TeamDao {

    val teams: List<Team>
        @Throws(NoFplResponseException::class)
        get() {
            val jsonBinder = Moshi.Builder().build()
            val teamsAdapter = jsonBinder.adapter(Team::class.java)

            try {
                val jsonString = IOUtils.toString(URL(FantasyEndpoint.STATIC.url), StandardCharsets.UTF_8)
                val json = JSONObject(jsonString)
                val teamsArray = json.getJSONArray("teams")
                val teamList = ArrayList<Team>()
                for (i in 0 until teamsArray.length()) {
                    val team: Team? = teamsAdapter.fromJson(teamsArray.get(i).toString())
                    if (team != null) {
                        teamList.add(team)
                    }
                }
                return teamList
            } catch (e: IOException) {
                throw NoFplResponseException(e.message, e)
            }
        }
}
