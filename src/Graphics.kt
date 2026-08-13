import dev.tamboui.style.Color
import dev.tamboui.toolkit.Toolkit.*
import dev.tamboui.toolkit.app.ToolkitApp
import dev.tamboui.toolkit.element.Element
import dev.tamboui.toolkit.elements.*
import dev.tamboui.widgets.input.TextInputState
import java.text.NumberFormat
import dev.tamboui.layout.Alignment
import dev.tamboui.toolkit.Toolkit
import kotlin.system.exitProcess


/**
 * @author js
 * @version crosby26
 *
 */
class Graphics : ToolkitApp() {

    /**
     * The prompts that the game makes
     */
    val prompts: MutableList<String> = mutableListOf()

    /**
     * The inputs that the user makes (needed to display the user inputs)
     */
    val inputs: MutableList<String> = mutableListOf()

    /**
     * The variable which saves the inputs that the user makes
     */
    val inputState: TextInputState = TextInputState()

    val game: DealOrNoDeal = DealOrNoDeal()

    var currentEvent:Events = Events(EventType.CASE_SAVING, "Save a case!")
    var lastEvent:Events = currentEvent

    private val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance()


    /**
     * Renders the game
     */
    protected override fun render(): Element {

        return template()

    }

    /**
     * The general template of how to run the game
     *
     * In the top half: The amounts in the cases is in the top left, the logo is above the case numbers remaining in the top right
     *
     * In the bottom half: The upper quarter holds the prompts, the bottom half holds inputs
     * @return The template of the game to [render()][render]
     */
    internal fun template(): Element {

        val placeholder: String = if (currentEvent.event != EventType.GAME_OVER) "Enter text here: " else "Press ENTER to quit."


        val values: List<TextElement> = game.cases.sorted().map {

            case -> if (case.selected == SelectionStatus.SELECTED) text(currencyFormat.format(case.amount)).fg(Color.hex("#3d3d3d")).alignment(Alignment.CENTER).fill()
            else text(currencyFormat.format(case.amount)).white().alignment(Alignment.CENTER).fill()

        }

        val caseIds: List<TextElement> = game.cases.sortedBy{case -> case.id}.map{
            case -> when (case.selected) {
                SelectionStatus.SAVED -> text(case.id).fg(Color.Rgb(87, 87, 0)).fill().alignment(Alignment.CENTER)
                SelectionStatus.NOT_SELECTED -> text(case.id).white().fill().alignment(Alignment.CENTER)
                SelectionStatus.SELECTED -> text(case.id).fg(Color.hex("#3d3d3d")).fill().alignment(Alignment.CENTER)
            }
        }

        //upcolumn
        val lowValueColumn: Column = column(*values.slice(0 until values.size/2).toTypedArray())
        val highValueColumn: Column = column(*values.slice(values.size/2 until values.size).toTypedArray())
        val valuePanel: Panel = panel("Money remaining", row(lowValueColumn.fill(), highValueColumn.fill()).fill()).rounded()

        val logoPanel: Panel = panel(text("DealOrNoDeal*"))
        val lowCaseRow: Row = row( *caseIds.slice(0 until caseIds.size/2).toTypedArray())
        val highCaseRow: Row = row(*caseIds.slice(caseIds.size/2 until caseIds.size).toTypedArray())
        val casePanel: Panel = panel("Cases", column(lowCaseRow.fill(), highCaseRow.fill()).fill()).rounded()

        val logoCaseColumn: Column = column(logoPanel.fill(2), casePanel.fill(3))

        //downcolumn
        val promptWindow: Panel = panel(text(if (currentEvent.event != EventType.BAD_INPUT) prompts.last() else prompts.last() + System.lineSeparator() + "Invalid input!"))

        val inputElem: TextInputElement = TextInputElement(inputState)
        val inputPanel: Panel = panel(inputElem.placeholder(placeholder).onSubmit {
            inputs.add(inputState.text())
            inputState.clear()

            if(currentEvent.event == EventType.GAME_OVER){

                quit()
                runner().tuiRunner().backend().clear()
                println("Thanks for playing!")
                exitProcess(0)

            }
            else if(currentEvent.event != EventType.BAD_INPUT){
                lastEvent = currentEvent
                currentEvent = game.logic(inputs.last(), currentEvent.event)
            }
            else{
                currentEvent = game.logic(inputs.last(), lastEvent.event)
            }

            prompts.add(currentEvent.msg)

        })


        val upRow: Row = row(valuePanel.fill(3), logoCaseColumn.fill(10))
        val downPanel: Panel = panel("Text Window", promptWindow.borderless(), inputPanel.borderless()).rounded()

        val display: Column = column(upRow.fill(2), downPanel.fill(1))

        return display


    }



    override fun run(): Unit {

        prompts.add("Select a case: ")


        super.run()
        return
    }


}

fun main(args: Array<String>) {

    val graphic: Graphics = Graphics()
    graphic.run()

}

