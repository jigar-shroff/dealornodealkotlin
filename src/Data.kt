

/**
 * A `Case` to be used in DealOrNoDeal
 * @param id The case number
 * @param amount The amount in the case
 * @param selected The [SelectionStatus] of the `Case`
 */
data class Case(val id:UInt, val amount: Double, var selected:SelectionStatus): Comparable<Case>{
    fun isSelected():Boolean{
        return selected != SelectionStatus.NOT_SELECTED
    }

    override fun compareTo(other: Case): Int {

        return if(amount < other.amount){
            -1
        } else if(amount == other.amount){
            0
        } else{
            1
        }

    }
}

/**
 * Selection status of a `Case`, needed because a `Case` has 3 states:
 * not selected, selected, and saved
 *
 * Enum — a fixed set of named constants.
 *
 * @constructor Creates a new SelectionStatus
 */
enum class SelectionStatus{
    NOT_SELECTED, SELECTED, SAVED
}

/**
 * A data class which models different types of events that can happen within the game, along with a message that can be passed to the user
 *
 * Used to communicate between the TUI and the game
 *
 * @constructor Makes a new instance using an [EventType] and a message
 * @property event The [EventType] that `this Events` instance is
 * @property msg The message to be delivered to the player with `this` Event
 */
data class Events(val event:EventType, val msg:String)

/**
 * The types of [Events] that can occur throughout the game
 *
 * @constructor Creates a new EventType
 */
enum class EventType{
    CASE_SAVING, CASE_SELECTION, BANKER, BAD_INPUT, GAME_OVER, SWAP
}
