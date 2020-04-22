package fpl.score

import data.Repository
import data.model.Footballer
import data.model.FootballerDaoImpl
import data.model.Opponent
import exception.NoFplResponseException
import java.io.IOException
import java.util.*

class FixtureCalculator(private val repo: Repository) {

    @Throws(IOException::class)
    fun getFixturesDifficulty(managerId: Int, weeksToCalculate: Int): MutableList<Footballer> {
        val footballers: MutableList<Footballer> = repo.getFootballers(managerId, repo.getGameWeek() - 1)
        for (i in 0 until weeksToCalculate) {
            updateOpposition(footballers, repo.getGameWeek() + i)
        }
        return footballers
    }

    @Throws(IOException::class)
    private fun updateOpposition(footballers: List<Footballer>, gameWeek: Int) {
        val playingFootballers = ArrayList<Footballer>()
        val footballerDao = FootballerDaoImpl()

        for (fixture in repo.getFixtures(gameWeek)) {
            val homeTeamId = fixture.team_h
            val homeTeamFixtureDifficulty = fixture.team_h_difficulty
            val awayTeamId = fixture.team_a
            val awayTeamFixtureDifficulty = fixture.team_a_difficulty

            val homeFootballers = footballerDao.getByTeamId(footballers, homeTeamId)
            for (footballer in homeFootballers) {
                val opponent = buildOpponent(awayTeamId, homeTeamFixtureDifficulty)
                footballerDao.update(homeFootballers, footballer.id, opponent)
            }

            val awayFootballers = footballerDao.getByTeamId(footballers, awayTeamId)
            for (footballer in awayFootballers) {
                val opponent = buildOpponent(homeTeamId, awayTeamFixtureDifficulty)
                footballerDao.update(awayFootballers, footballer.id, opponent)
            }

            playingFootballers.addAll(homeFootballers)
            playingFootballers.addAll(awayFootballers)
        }

        val nonPlayingFootballers = footballerDao.getInverse(footballers, playingFootballers)
        for (footballer in nonPlayingFootballers) {
            val opponent = buildOpponent(-1, 0)
            footballerDao.update(nonPlayingFootballers, footballer.id, opponent)
        }
    }

    @Throws(NoFplResponseException::class)
    private fun buildOpponent(oppositionId: Int, difficultyRating: Int): Opponent {
        val opponent = Opponent()
        opponent.difficultyRating = difficultyRating

        if (oppositionId == -1) {
            opponent.name = "NO FIXTURE"
            opponent.teamId = 0
        } else {
            opponent.teamId = oppositionId
            val teamList = repo.getTeams()
            for (team in teamList) {
                if (oppositionId == team.id) {
                    opponent.name = team.short_name
                }
            }
        }
        return opponent
    }
}
