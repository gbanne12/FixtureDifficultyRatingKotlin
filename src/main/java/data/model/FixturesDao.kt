package data.model

import com.squareup.moshi.Moshi
import exception.NoFplResponseException
import fpl.url.FantasyEndpoint
import org.apache.commons.io.IOUtils
import org.json.JSONArray
import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

class FixturesDao {

    @Throws(NoFplResponseException::class)
    fun getAllFixtures(gameWeek: Int): List<Fixture> {
        val jsonBinder = Moshi.Builder().build()
        val fixturesAdapter = jsonBinder.adapter(Fixture::class.java)

        try {
            val jsonString = IOUtils.toString(URL(FantasyEndpoint.FIXTURES.url + gameWeek), StandardCharsets.UTF_8)
            val fixturesArray = JSONArray(jsonString)
            val fixtures = ArrayList<Fixture>()
            for (i in 0 until fixturesArray.length()) {
                val fixture: Fixture? = fixturesAdapter.fromJson(fixturesArray.get(i).toString())
                if (fixture != null) {
                    fixtures.add(fixture)
                }
            }
            return fixtures

        } catch (e: IOException) {
            throw NoFplResponseException(e.message, e)
        }
    }
}
