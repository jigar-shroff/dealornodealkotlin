//import java.sql.DriverManager.println
import java.util.ArrayList
import java.util.Random
//import kotlin.io.*

class DealOrNoDeal {
    
    private var cases:Array<Case> = emptyArray<Case>()
    private var selectedCase:Int = -1

    constructor(sc:Int){

        var numbers:ArrayList<Int> = ArrayList<Int>()

        for(i in 0..14-1){
            numbers.add(i)
        }

        var init:Array<Double> = arrayOf<Double>(0.01, 0.50, 1.0, 5.0,
            100.0, 500.0, 1000.0, 10000.0, 50000.0, 100000.0, 250000.0, 500000.0, 750000.0, 1000000.0)
        
        for(i in 0..init.size-1){
            var randomIndex:Int = (Math.random()*14).toInt()
            var caseNum:Int = numbers.removeAt(randomIndex)

            cases[caseNum] = Case(false, init[i])


        }

        selectedCase = sc






    }

    /**
     * Assume case is off by one
     * @param case The number of the case being selected, off by one
     * @return none
     */
    fun selectCase(case:Int){

        val selectedCase = case - 1;

        try {


            if(cases[selectedCase].isSelected || selectedCase==this.selectedCase){
                println("This case has already been selected!")
                return
            }

            cases[selectedCase].isSelected = true
        }
        catch(e: ArrayIndexOutOfBoundsException){

            println("The case is invalid")
            return

        }

        println("You have selected case number: $case")
        print("The value inside the case was...")

        Thread.sleep(5000)

        println("$${cases[selectedCase].amount}")

        var caseValues:Array<Double> = cases.map(){case -> case.amount}.sorted().toTypedArray()
        var median = caseValues[caseValues.size/2]

        if(cases[selectedCase].amount < median){

            println("Great!")


        }
        else{
            println("Too bad!")
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

        var caseToSwapWith:Int = -1

        for(r in 0..cases.size-1){

            if(cases[r].isSelected == false && r != selectedCase){

                caseToSwapWith = r
                break

            }

        }

        if(caseToSwapWith == -1){
            return -1.0
        }
        else{

            val temp:Int = selectedCase
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
        var highLowMiddle:Int = (Math.random()*3).toInt() - 2
        var randValue: Double = rand.nextDouble()


        var average:Double = 0.0

        for(r in 0..cases.size-1){
            if(cases[r].isSelected == false){

                average += cases[r].amount


            }
        }

        average /= cases.size


        var deal:Double

        when(randValue){
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
            in 0.6 .. 0.9 -> {
                val plusMinus:Boolean = rand.nextBoolean()

                average = if (plusMinus) 0.77*average else 1.32*average
                return average
            }
            in 0.9 .. 1.0 -> {
                val plusMinus:Boolean = rand.nextBoolean()

                average = if (plusMinus) 0.17*average else 1.47*average

                return average
            }
            else -> return 5*average

        }


    }
    
    
    fun main() {

        print("Select a case: ")
        var sc:Int = readLine()?.toInt()?:0

        var game:DealOrNoDeal = DealOrNoDeal(sc)



    }
    
}

data class Case(var isSelected:Boolean, val amount:Double)