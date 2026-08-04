//import java.sql.DriverManager.println
import java.text.NumberFormat
import java.util.*
import kotlin.system.exitProcess

//import kotlin.io.*

class DealOrNoDeal {

    private var cases: Array<Case> = Array<Case>(14) { _ -> Case(false, 0.0) }
    private var selectedCase: Int = -1

    private val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance()


    /**
     * Constructor for the game
     * @param sc2 The case that has been selected (off by one)
     */
    constructor(scOffByOne: Int) {

        val sc: Int = scOffByOne - 1

        var numbers: ArrayList<Int> = ArrayList<Int>()

        for (i in 0 until 14) {
            numbers.add(i)
        }

        var init: Array<Double> = arrayOf<Double>(
            0.01, 0.50, 1.0, 5.0,
            100.0, 500.0, 1000.0, 10000.0, 50000.0, 100000.0, 250000.0, 500000.0, 750000.0, 1000000.0
        )

        for (i in 0 until init.size) {
            var randomIndex: Int = (Math.random() * numbers.size).toInt()
            var caseNum: Int = numbers.removeAt(randomIndex)

            cases[caseNum] = Case(false, init[i])


        }

        selectedCase = sc


    }

    /**
     * Assume case is off by one
     * @param case The number of the case being selected, off by one
     * @return none
     */
    fun selectCase(case: Int) {

        val selectedCase = case - 1;

        try {


            if (cases[selectedCase].isSelected || selectedCase == this.selectedCase) {
                if (!enoughCasesSelected())
                    throw IllegalStateException("This case has already been selected!")
                else return
            }

            cases[selectedCase].isSelected = true
        } catch (e: ArrayIndexOutOfBoundsException) {

            throw IllegalStateException("This case is invalid!")

        }

        println("You have selected case number: $case")
        print("The value inside the case was...")

        Thread.sleep(2000)

        println("${currencyFormat.format(cases[selectedCase].amount)}")

        var caseValues: Array<Double> = cases.map() { case ->
            case.amount
        }.sorted().toTypedArray()
        var median = caseValues[caseValues.size / 2]

        if (cases[selectedCase].amount < median) {

            println("Great!")


        } else {
            println("Too bad!")
        }


        val filtered: List<Case> = cases.filter { case -> !case.isSelected }.sortedBy { case -> case.amount }

        print("Values remaining -> ")
        for ((index, value) in filtered.withIndex()) {

            if (index == filtered.size - 1) {
                println("${currencyFormat.format(value.amount)}")
            } else {
                print("${currencyFormat.format(value.amount)}, ")
            }

        }

//        val filtered2:List<Case> = filtered.filterIndexed { index, case -> index != selectedCase }.sortedBy{case -> case.}

        print("Cases to select -> ")

        for (r: Int in 0 until cases.size) {

            if (!cases[r].isSelected && r != this.selectedCase) {
                print("${r + 1} ")
            }

        }

        println()

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

            if (cases[r].isSelected == false && r != selectedCase) {

                caseToSwapWith = r
                break

            }

        }

        if (caseToSwapWith == -1) {
            return -1.0
        } else {

            val temp: Int = selectedCase
            selectedCase = caseToSwapWith

            return cases[temp].amount
        }


    }

    /**
     * Deal that the banker makes
     * @return the deal
     */
    private fun bankerDeal(): Double {

        var rand: Random = Random()
        var highLowMiddle: Int = (Math.random() * 3).toInt() - 2
        var randValue: Double = rand.nextDouble()


        var average: Double = 0.0

        for (r in 0..cases.size - 1) {
            if (cases[r].isSelected == false) {

                average += cases[r].amount


            }
        }

        average /= cases.size


        var deal: Double

        when (randValue) {
//
//            -1 -> {
//
//                deal = Math.random()*median
//
//
//            }
            in 0.0..0.6 -> {

                return average

            }

            in 0.6..0.9 -> {
                val plusMinus: Boolean = rand.nextBoolean()

                average = if (plusMinus) 0.77 * average else 1.32 * average
                return average
            }

            in 0.9..1.0 -> {
                val plusMinus: Boolean = rand.nextBoolean()

                average = if (plusMinus) 0.17 * average else 1.47 * average

                return average
            }

            else -> return 5 * average

        }


    }
    /*
        Returns true if and only if all but two of the cases are selected
     */

    private fun enoughCasesSelected(): Boolean {

        var unselectedCases: Int = 0

        for (case: Case in cases) {
            if (!(case.isSelected)) {
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

        for (r: Int in 0..cases.size - 1) {

            if (!cases[r].isSelected && r != selectedCase) {

                return cases[r].amount

            }

        }

        return -1.0


    }


    fun driver(): Unit {

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


    }

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


data class Case(var isSelected: Boolean, val amount: Double)

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

}