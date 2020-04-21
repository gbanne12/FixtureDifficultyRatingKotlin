package data

import data.model.Element
import data.model.Fixture
import data.model.Footballer
import data.model.Team
import exception.NoFplResponseException

interface Repository {

    @Throws(NoFplResponseException::class)
    fun getElements(): List<Element>

    @Throws(NoFplResponseException::class)
    fun getTeams(): List<Team>

    @Throws(NoFplResponseException::class)
    fun getGameWeek(): Int

    @Throws(NoFplResponseException::class)
    fun getFootballers(teamId: Int, gameWeek: Int): List<Footballer>

    @Throws(NoFplResponseException::class)
    fun getFixtures(gameWeek: Int): List<Fixture>
}
