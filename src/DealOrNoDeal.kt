//import java.sql.DriverManager.println
import java.text.NumberFormat
import java.util.*
import kotlin.system.exitProcess

//import kotlin.io.*

/**
 * A class that creates a game of Deal or No Deal, as per the rules of the existing game show
 */
class DealOrNoDeal {

    /**
     * The [Cases][Case] that are used in the game
     *
     * Each `Case` stores its value and its selection status
     */
    internal lateinit var cases: Array<Case>

    /**
     * The index of the case that is selected
     *
     * In other words `cases[selectedCase].selected = SelectionStatus.SAVED`
     */
    private var selectedCase: Int = -1

    //The currency formatter that formats based on Locale
    private val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance()
    //The amount of turns until the banker calls
    private var bankerCalls:Int = 8

    //The amount the banker gave in their last deal
    private var dealAmount:Double = 0.0




    /**
     * An initialization block which creates [cases] by randomizing the amounts in each case
     */
    init {
        // logic that executes whenever a constructor gets called
        // constructor assigns, init block executes



        var numbers: ArrayList<Int> = ArrayList<Int>()

        for (i in 0 until 14) {
            numbers.add(i)
        }

        var init: Array<Double> = arrayOf<Double>(
            0.01, 0.50, 1.0, 5.0,
            100.0, 500.0, 1000.0, 10000.0, 50000.0, 100000.0,
            250000.0, 500000.0, 750000.0,
            1000000.0
        )

        var casesList:MutableList<Case> = mutableListOf()

        for (i in init.indices) {
            var randomIndex: Int = (Math.random() * numbers.size).toInt()
            var caseNum: Int = numbers.removeAt(randomIndex)

            casesList.add(Case(caseNum.toUInt() + 1u, init[i], SelectionStatus.NOT_SELECTED))

            //cases[caseNum] = Case(caseNum.toUInt(), init[i], SelectionStatus.NOT_SELECTED)

        }

        casesList.sortBy{c -> c.id}
        cases = casesList.toTypedArray()
    }

    /**
     * Selects the case whose id is given by parameter
     * @param selectedCase The number of the case being selected
     * @return The next [Event][Events] that should occur
     */
    fun selectCase(selectedCase: Int):Events {


//        if(enoughCasesSelected()){
//            return Events(EventType.SWAP, "There are 2 cases left, do you want to swap?")
//        }

        try {

            if (cases[selectedCase].isSelected() /*|| selectedCase == this.selectedCase*/) {
                return Events(EventType.BAD_INPUT, "Case has been selected already!")
            }

            cases[selectedCase].selected = SelectionStatus.SELECTED
            bankerCalls--
        }
        catch (e: ArrayIndexOutOfBoundsException) {

            return Events(EventType.BAD_INPUT, "Case index out of range")

        }

        return if(bankerCalls == 0) {
//            var BANKER_CALLING:Events = Events(EventType.BANKER, "The banker is calling and is offering ${bankerDeal()}")
//            return BANKER_CALLING
            val bankerEvent:Events = bankerLogic()
            val modifiedEvent: Events = Events(bankerEvent.event, "That case had ${currencyFormat.format(cases[selectedCase].amount)}" + System.lineSeparator() + bankerEvent.msg)
            modifiedEvent
        }
        else if(enoughCasesSelected()){
            Events(EventType.SWAP, "That case had ${currencyFormat.format(cases[selectedCase].amount)}.\nThere are 2 cases left, do you want to swap?")
        }
        else if(bankerCalls > cases.filter{c:Case -> !c.isSelected()}.size-1){
            Events(EventType.CASE_SELECTION, "That case had ${currencyFormat.format(cases[selectedCase].amount)}.\nSelect a case:")
        }
        else{
            Events(EventType.CASE_SELECTION, "That case had ${currencyFormat.format(cases[selectedCase].amount)}.\nSelect a case (The banker is calling in: $bankerCalls turns):")
        }


        //println("You have selected case number: $case")
        //print("The value inside the case was...")

        //Thread.sleep(2000)

        //println("${currencyFormat.format(cases[selectedCase].amount)}")

       /* var caseValues: Array<Double> = cases.map() { case ->
            case.amount
        }.sorted().toTypedArray()*/
        //var median = caseValues[caseValues.size / 2]

        /*if (cases[selectedCase].amount < median) {

            println("Great!")


        } else {
            println("Too bad!")
        }*/


        //val filtered: List<Case> = cases.filter { case -> !case.isSelected() }.sortedBy { case -> case.amount }

       /* print("Values remaining -> ")*/
       /* for ((index, value) in filtered.withIndex()) {

            if (index == filtered.size - 1) {
                println("${currencyFormat.format(value.amount)}")
            } else {
                print("${currencyFormat.format(value.amount)}, ")
            }

        }*/

//        val filtered2:List<Case> = filtered.filterIndexed { index, case -> index != selectedCase }.sortedBy{case -> case.}

       /* print("Cases to select -> ")

        for (r: Int in 0 until cases.size) {

            if (!cases[r].isSelected() && r != this.selectedCase) {
                print("${r + 1} ")
            }

        }

        println()*/

    }

    /**
     * Handles the logic of sending [Events] based on [EventTypes][EventType] that are being sent in, along with the player's [response]
     *
     * @param response The player's response
     * @param event The [EventType] that is occuring
     * @return The next [Event][Events] that is to occur
     * @throws IllegalStateException If an invalid [EventType] is sent in
     */
    @Throws(IllegalStateException::class)
    internal fun logic(response:String, event:EventType):Events{



        when(event){


            EventType.CASE_SELECTION ->{

                //validate
                var case:Int = 0
                try {
                    case = response.toInt()
                }
                catch(e: NumberFormatException){
                    return Events(EventType.BAD_INPUT, "Input a number!")
                }


                if(--case !in 0..14){
                    return Events(EventType.BAD_INPUT, "Input is out of range.")
                }

                return selectCase(case)


            }

            EventType.CASE_SAVING -> {

                var case:Int = 0

                try{
                    case = response.toInt()
                }
                catch(e:NumberFormatException){
                    return Events(EventType.BAD_INPUT, "Input a number!")
                }

                if(--case !in 0..14) return Events(EventType.BAD_INPUT, "Input is out of range")

                selectedCase = case
                cases[selectedCase].selected = SelectionStatus.SAVED

                return Events(EventType.CASE_SELECTION, "Select a case:")

            }
            EventType.BANKER -> {

                val yesNo:Char = response.lowercase()[0]

                if(yesNo != 'y' && yesNo != 'n')
                    return Events(EventType.BAD_INPUT, "Input 'y' or 'n'.")

                if(yesNo == 'y'){

                    var msg:String = "You accepted the banker's deal of ${currencyFormat.format(dealAmount)}" +
                            "\n Inside of your case there was ${currencyFormat.format(cases[selectedCase].amount)}"

                    if(dealAmount > cases[selectedCase].amount){
                        msg += "\nGood job!"
                    }
                    else{
                        msg += "\nToo bad!"
                    }

                    return Events(EventType.GAME_OVER, msg)


                }
                else{
                    bankerCalls = (Math.random()*4).toInt()+1
                    return if(bankerCalls > cases.filter{c:Case -> !c.isSelected()}.size){
                         Events(EventType.CASE_SELECTION, "Select a case:")
                    }
                    else{
                        Events(EventType.CASE_SELECTION, "Select a case (The banker is calling in $bankerCalls turns):")
                    }
                }



            }
            EventType.SWAP ->{

                when(response.lowercase()){
                    "y" -> swap()
                    "n" -> {}
                    else -> return Events(EventType.BAD_INPUT, "Input 'y' or 'n'.")
                }

                val otherAmount:Double = findOtherCase()
                var msg:String = "The other case had ${currencyFormat.format(otherAmount)} in it! \n" +
                        "Your case had ${currencyFormat.format(cases[selectedCase].amount)}"
                if(cases[selectedCase].amount > otherAmount){
                    msg += "\n Good work!"
                }
                else{
                    msg += "\n Yikes!"
                }

                return Events(EventType.GAME_OVER, msg)


            }
         else -> {
             throw IllegalStateException("Should not be able to pass in $event")
         }
        }
    }

    /**
     * Swaps with the last remaining case
     *
     * Precondition: There's only one case remaining
     *
     * @return The value of selectedCase, or -1, if the swap was not successful
     */
    private fun swap(): Double {

        var caseToSwapWith: Int = -1

        for (r in 0..cases.size - 1) {

            if (/*cases[r].isSelected() == false && r != selectedCase*/
                !cases[r].isSelected()
            ) {

                caseToSwapWith = r
                break

            }

        }

        if (caseToSwapWith == -1) {
            return -1.0
        }
        else {

            val temp: Int = selectedCase

            cases[selectedCase].selected = cases[caseToSwapWith].selected

            selectedCase = caseToSwapWith
            cases[caseToSwapWith].selected = SelectionStatus.SAVED

            return cases[temp].amount
        }


    }

    /**
     * Deal that the banker makes
     * @return the deal
     */
    private fun bankerLogic(): Events {

        var rand: Random = Random()
        //var highLowMiddle: Int = (Math.random() * 3).toInt() - 2
        val randValue: Double = rand.nextDouble()


        val average: Double = cases.filter {
            c: Case -> !c.isSelected()
        }.sumOf{
            c:Case -> c.amount
        } / cases.filter{ c:Case -> !c.isSelected()}.size

        dealAmount = when (randValue) {
    //
    //            -1 -> {
    //
    //                deal = Math.random()*median
    //
    //
    //            }
            in 0.0..0.4 -> {

                average

            }

            in 0.4..0.9 -> {
                val plusMinus: Boolean = rand.nextBoolean()

                if (plusMinus) 0.62 * average else 1.15 * average

            }

            in 0.9..0.950 -> {
                val plusMinus: Boolean = rand.nextBoolean()

                if(plusMinus) 0.17 * average else 2 * average


            }

            else -> 5 * average

        }


        return Events(EventType.BANKER, "The banker has called!" +
                "\nThey offered ${currencyFormat.format(dealAmount)}" +
                "\nAccept? (y/n)")

    }
    /*
        Returns true if and only if all but two of the cases are selected
     */

    private fun enoughCasesSelected(): Boolean {

        var unselectedCases: Int = 0

        for (case: Case in cases) {
            if (case.selected != SelectionStatus.SELECTED) {
//                println("[!]" + case.amount + " | " + case.isSelected)
                unselectedCases++
            }
        }

//        print(unselectedCases)

        if (unselectedCases < 2) throw IllegalStateException()
        return unselectedCases == 2

    }


    /*
     * If there are only two cases remaining, this function will find the case that IS NOT {@code selectedCase}
     * @return The value in the unselected Case or -1.0 if the search was unsuccessful
     */
    private fun findOtherCase(): Double {

        if (!enoughCasesSelected()) {
            return -1.0
        }

        return cases.filter { c: Case ->
            c.selected == SelectionStatus.NOT_SELECTED
        }[0].amount

//        for (r: Int in 0..cases.size - 1) {
//
//            if (cases[r].selected != SelectionStatus.SELECTED) {
//
//                return cases[r].amount
//
//            }
//
//        }

    }
   /* fun driver(): Unit {

//        var game:DealOrNoDeal = DealOrNoDeal(sc)

        var untilBankerCalls: Int = (8 * Math.random()).toInt() + 1
//        var numCalls:Byte = 6

        while (!enoughCasesSelected()) {
            //println(game.enoughCasesSelected())


            if (untilBankerCalls == 0) {
//                numCalls--
                val deal: Float = bankerDeal().toFloat()

                println("The banker is offering ${currencyFormat.format(deal)}")

                print("Will you accept? (y/n): ")

                val inp: String = readln()

                if (inp.startsWith("y")) {
                    println("You've accepted the deal...")
                    Thread.sleep(1000)

                    println("The banker offered ${currencyFormat.format(deal)}")
                    Thread.sleep(500)

                    print("In case $selectedCase, there was ")
                    Thread.sleep(2000)

                    println("${currencyFormat.format(cases[selectedCase].amount)}")

                    if (cases[selectedCase].amount > deal) {
                        error("Bad intuition?")
                        //exitProcess(-1)
                    } else {
                        println("Psychic?")
                        exitProcess(0)
                    }

                }

                untilBankerCalls = (8 * Math.random()).toInt() + 1

            } else {

                do {

                    var runAgain: Boolean = false

                    print("Select a case: ")
                    try {
                        selectCase(readln().toInt())
                    } catch (e: RuntimeException) {
                        println(e.localizedMessage)
                        runAgain = true; untilBankerCalls++
                    }
                    untilBankerCalls--

                    println("The banker will call in ${untilBankerCalls} turns")


                } while (runAgain)
            }


        }

        var swapOrNot: String

        do {
            print("Do you want to swap?(y/n) ")
            swapOrNot = readlnOrNull()?.lowercase()?.substring(0 until 1) ?: ""
            println()
        } while (swapOrNot != "y" && swapOrNot != "n")


        val amountInOther: Double = when (swapOrNot) {

            "y" -> {
                swap()
            }

            "n" -> {
                findOtherCase()
            }

            else -> {
                throw IllegalStateException()
            }

        }

        Thread.sleep(2000)

        print("The amount in the other case was...")
        Thread.sleep(2000)

        println("${currencyFormat.format(amountInOther)}")

        print("The amount in your case is...")
        Thread.sleep(2000)

        println("${currencyFormat.format(cases[selectedCase].amount)}")

        if (amountInOther > cases[selectedCase].amount) {
            error("Too bad!")
        } else {
            println("Good work!")
            exitProcess(0)
        }


    }*/

    override fun toString(): String {
        return "DealOrNoDeal(selectedCase=$selectedCase, cases=${cases.contentToString()})"
    }

    override fun equals(other: Any?): Boolean {

        if (other is DealOrNoDeal) {

            return other.selectedCase == selectedCase && other.cases.contentEquals(cases)


        }

        return false
    }


    override fun hashCode(): Int {

        var hash: Int = 17

        hash *= cases.contentHashCode() * 37
        hash *= selectedCase * 31

        return hash

    }


}


//data class Case(var isSelected: Boolean, val amount: Double)

/*
fun main(args: Array<String>): Unit {

    var valid: Boolean = true
    var sc: Int = -1

    do {
        try {
            print("Select a case: ")
            sc = readln().toInt()
            if (sc !in 1..14) {
                valid = false
            }
        } catch (e: NumberFormatException) {
            valid = false
        }
    } while (!valid)

    val obj: DealOrNoDeal = DealOrNoDeal(sc)

    obj.driver()

}*/
