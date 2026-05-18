//import java.sql.DriverManager.println
import java.util.ArrayList
//import kotlin.io.*

class DealOrNoDeal {
    
    private var cases:Array<Case> = emptyArray<Case>()
    private var selectedCase:Int = -1

    constructor(){
        
        var init:Array<Double> = arrayOf<Double>(0.01, 0.50, 1.0, 5.0,
            100.0, 500.0, 1000.0, 10000.0, 50000.0, 100000.0, 250000.0, 500000.0, 750000.0, 1000000.0)
        
        for(i in 0..init.size){

            cases[i] = Case(false, init[i])


        }




    }

    /**
     * Assume case is off by one
     * @param case The number of the case being selected, off by one
     * @return none
     */
    fun selectCase(case:Int){

        try {
            selectedCase = case - 1;
            cases[selectedCase].isSelected = true
        }
        catch(e: ArrayIndexOutOfBoundsException){

            println("The case is invalid")
            return

        }



    }
    
    
    
    
    
    fun main() {
        


    }
    
}

class Case{
    
    var isSelected:Boolean
    var amount:Double



    constructor(s:Boolean, n:Double){
        
        isSelected = s
        amount = n
        
    }
    
    
    
    
    
    
    
    
}