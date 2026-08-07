data class Case(val id:UInt, val amount:UInt, val selected:SelectionStatus){
    fun isSelected():Boolean{
        return selected != SelectionStatus.NOT_SELECTED
    }
}
data class Values(val money:UInt, val isSelected:Boolean)
enum class SelectionStatus{
    NOT_SELECTED, SELECTED, SAVED
}

