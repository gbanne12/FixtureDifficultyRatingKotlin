package data.model

class Opponent {

    var teamId: Int = 0
    var difficultyRating: Int = 0
    var name: String? = null

    override fun toString(): String {
        return "team: $name | difficulty: $difficultyRating"
    }

    override fun equals(any: Any?): Boolean {
        val opponent = any as Opponent?
        if (opponent != null) {
            return opponent.teamId == teamId &&
                    opponent.name == name &&
                    opponent.difficultyRating == difficultyRating
        }
        return false
    }
}
