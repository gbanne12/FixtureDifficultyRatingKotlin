package data.model

import java.util.ArrayList


class Footballer : Comparable<Footballer> {

    var id: Int = 0
    var teamId: Int = 0
    var position: Int = 0
    var webName: String? = null
    var opponentList: MutableList<Opponent> = ArrayList()
    val difficultyTotal: Int
        get() {
            var total = 0
            for (opponent in opponentList) {
                total += opponent.difficultyRating
            }
            return total
        }

    override fun compareTo(other: Footballer): Int {
        return Integer.compare(difficultyTotal, other.difficultyTotal)
    }
}
