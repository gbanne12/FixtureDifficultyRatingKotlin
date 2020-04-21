package data.model

class Opponent {

    var teamId: Int = 0
    var difficultyRating: Int = 0
    var name: String? = null

    override fun toString(): String {
        return "team: $name | difficulty: $difficultyRating"
    }
}
