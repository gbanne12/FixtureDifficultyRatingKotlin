package data

import data.model.*
import exception.NoFplResponseException
import java.io.IOException
import java.util.*

/**
 * Repository for data retrieved directly from the FPL endpoints
 * i.e. held in memory only, not saved anywhere.
 */
class TransientRepository : Repository {

    private var week: Int = 0
    private var footballers: MutableList<Footballer> = ArrayList()
    private var elements: List<Element> = ArrayList()
    private var fixtures: List<Fixture> = ArrayList()
    private var teams: List<Team> = ArrayList()

    @Throws(NoFplResponseException::class)
    override fun getElements(): List<Element> {
        if (!elements.isEmpty()) {
            return elements
        }
        elements = ElementsDao().elements
        return elements
    }

    @Throws(NoFplResponseException::class)
    override fun getFootballers(teamId: Int, gameWeek: Int): MutableList<Footballer> {
        if (!footballers.isEmpty()) {
            return footballers
        }
        footballers = FootballerDaoImpl().getAll(teamId, gameWeek)
        val elements: List<Element> = getElements()
        for(element in elements) {
            for (footballer in footballers) {
                if (element.id == footballer.id) {
                    footballer.teamId = element.team
                    footballer.webName = element.web_name
                }
            }
        }
        return footballers
    }

    /**
     * Get the fixtures for a given 'gameweek'
     * Note: a gameweek is in no way tied to a calendar week
     * @param gameWeek the 'week' number of the fixtures
     * @return List of fixtures containing the home and away team IDs
     * @throws IOException when response cannot be obtained from the endpoint
     */
    @Throws(NoFplResponseException::class)
    override fun getFixtures(gameWeek: Int): List<Fixture> {
        val isCurrentGameWeek = (gameWeek == getGameWeek())
        val isCurrentFixtures = isCurrentGameWeek && !fixtures.isEmpty()
        if (isCurrentFixtures) {
            return fixtures
        }
        fixtures = FixturesDao().getAllFixtures(gameWeek)
        return fixtures
    }

    @Throws(NoFplResponseException::class)
    override fun getTeams(): List<Team> {
        if (!teams.isEmpty()) {
            return teams
        }
        teams = TeamDao().teams
        return teams
    }

    /**
     * Get the currently active game week number
     * Will return the first unfinished game week, so an in-progress but unfinished week will be returned
     *
     * @return number of the first unfinished game week
     * @throws IOException
     */
    @Throws(NoFplResponseException::class)
    override fun getGameWeek(): Int {
        /*if (week > 0) {
            return week
        }
        week = EventDao().currentWeek
        return week*/
        return 2
    }
}
