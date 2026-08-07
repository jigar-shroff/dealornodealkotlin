import dev.tamboui.toolkit.Toolkit.*
import dev.tamboui.toolkit.app.ToolkitApp
import dev.tamboui.toolkit.element.Element
import dev.tamboui.toolkit.elements.Column
import dev.tamboui.toolkit.elements.Panel
import dev.tamboui.toolkit.elements.Row
import dev.tamboui.toolkit.elements.*
import dev.tamboui.widgets.input.TextInputState

/**
 * @author js
 * @version crosby26
 *
 */
class Graphics: ToolkitApp() {

    val prompts:MutableList<String> = mutableListOf()
    val inputs:MutableList<String> = mutableListOf()

    val inputState: TextInputState = TextInputState()
    val placeholder:String = "Enter text here: "

    val amounts:MutableList<Values> = mutableListOf()

    /**
     * Renders the game
     */
    protected override fun render(): Element {

        return template()

    }


    internal fun template(): Element{

        //upcolumn
        val lowValueColumn:Column = column(text(/*insert text here*/))
        val highValueColumn:Column = column(text(/*insert text here*/))
        val valuePanel: Panel = panel("Money remaining",lowValueColumn.fill(1), highValueColumn.fill(1)).rounded()

        val logoPanel:Panel = panel(text("DealOrNoDeal*"))
        val lowCaseRow:Row =  row(text(/*insert text*/))
        val highCaseRow:Row = row(text(/*insert text*/))
        val casePanel:Panel = panel("Cases", column(lowCaseRow.fill(1), highCaseRow.fill(1))).rounded()

        val logoCaseColumn:Column = column(logoPanel.fill(1), casePanel.fill(2))

        //downcolumn
        val promptWindow:Panel = panel(*prompts.map{
            str -> text(str)
        }.toTypedArray())

        val inputElem:TextInputElement = TextInputElement(inputState)
        val inputPanel:Panel = panel(inputElem.placeholder(placeholder).onSubmit{
            inputs.add(inputState.text())
            //inputState.clear()
            game()
        })


        val upRow:Row = row(valuePanel.fill(1), logoCaseColumn.fill(5))
        val downPanel:Panel = panel("Text Window", promptWindow.borderless(), inputPanel.borderless()).rounded()

        val display:Column = column(upRow.fill(2), downPanel.fill(1))

        return display



    }

    fun runApp():Unit{

        this.run()
        return
    }


    fun game(){

    }


}

fun main(args:Array<String>){

    val graphic:Graphics = Graphics()
    graphic.runApp()

}