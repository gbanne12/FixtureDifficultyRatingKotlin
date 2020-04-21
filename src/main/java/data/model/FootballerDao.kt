package data.model

import java.io.IOException

interface FootballerDao {

    @Throws(IOException::class)
    fun getAll(managerId: Int, gameWeek: Int): List<Footballer>

    fun update(footballers: List<Footballer>, id: Int, opponent: Opponent)
}
