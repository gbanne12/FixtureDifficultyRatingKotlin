package data.model


import com.squareup.moshi.Moshi
import java.io.IOException
import java.util.*
import java.util.stream.Collectors

class FootballerDaoImpl : FootballerDao {

    @Throws(IOException::class)
    override fun getAll(managerId: Int, gameWeek: Int): List<Footballer> {
        val picks = PicksDao().getPicks(managerId, gameWeek)
        val moshi = Moshi.Builder().build()
        val picksAdapter = moshi.adapter(Pick::class.java)

        val footballers = ArrayList<Footballer>()
        for (i in 0 until picks.length()) {
            val pick = picksAdapter.fromJson(picks.get(i).toString())
            if (pick != null) {
                val footballer = Footballer()
                footballer.id = pick.element
                footballer.position = pick.position
                footballers.add(footballer)
            }
        }
        return footballers
    }

    override fun update(footballers: List<Footballer>, id: Int, opponent: Opponent) {
        val footballer = footballers.stream()
                .filter { x -> id == x.id }
                .limit(1)
                .collect(Collectors.toList())[0]
        footballer.opponentList.add(opponent)
    }

    fun getInverse(footballers: List<Footballer>, toInvert: List<Footballer>): List<Footballer> {
        val existingIds = toInvert
                .stream()
                .map(Footballer::id)
                .collect(Collectors.toSet())

        return footballers
                .stream()
                .filter { footballer -> !existingIds.contains(footballer.id) }
                .collect(Collectors.toList())
    }

    fun getByTeamId(footballers: List<Footballer>, teamId: Int): List<Footballer> {
        return footballers.stream()
                .filter { x -> teamId == x.teamId }
                .limit(3)
                .collect(Collectors.toList())
    }

}
