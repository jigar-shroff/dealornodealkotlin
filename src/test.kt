import dev.tamboui.toolkit.Toolkit.*
import dev.tamboui.toolkit.app.ToolkitApp
import dev.tamboui.toolkit.element.Element
import dev.tamboui.toolkit.elements.Column
import dev.tamboui.toolkit.elements.Panel
import dev.tamboui.toolkit.elements.Row
import dev.tamboui.toolkit.elements.TextInputElement
import dev.tamboui.widgets.input.TextInputState

class HelloDsl : ToolkitApp() {


    var inputRender:Panel = Panel()
    var inpState: TextInputState = TextInputState()
    var inpElem: TextInputElement = TextInputElement(inpState)

    val msg:MutableList<String> = mutableListOf<String>()


    protected override fun render(): Element {

        return rowsColumnsTest()
        //return row(panel("Left").fill(), panel("Right").fill())

//        return panel("Hello",
//            text("Welcome to --").bold().cyan(),
//            spacer(),
//            text("Press q to quit?").dim()
//        ).rounded()


    }

//    init{
//        textInput(inputState).onSubmit{}
//    }

    /**
     * Creates rows and columns
     * @return The text
     */
    fun rowsColumnsTest():Element{




        val rightUpUp:Element = panel("B", column(
    *msg.map{
                string-> text(string)
            }.toTypedArray()
        ))

        val rightUp: Column = column(rightUpUp, panel("RightDownUp").fill(3))

        val rows:Row = row(panel(
            "LeftUp").fill(1),
            spacer(1),
            rightUp.fill(3)
        )


//        val input: TextInputState = TextInputState("Input!")
//        val inputPanel: TextInputElement = textInput(input).text("Input!")

//        val builtTextInput:TextInput = TextInput.builder().placeholder("Input?")
//            .block(Block.builder()
//                .title("??")
//                .borders(Borders.ALL)
//                .build()
//            ).build()

        //val rectangle: Rect = Rect(10, 10, 10, 10)
        //val buff: Buffer = Buffer.empty(rectangle)

        inpElem = TextInputElement(inpState).placeholder("Input? Pleeeeeaaaase???").rounded()
        val columns: Column = column(rows.fill(9), inpElem.fill(4).title("AAAAA").onSubmit{
            msg.add(inpState.text())
            inpState.clear()

        }

//            textInput(inputState).placeholder("A").onSubmit {
//                -> handler(inputState.text())
//            }.fill(16)
        )

       // builtTextInput.render(rectangle, buff, inputState)

        return columns


    }

    /**
     * Tests the input function [TextInputState]
     * @return If it was successful?
     */
    internal fun handler(input:String?):Unit{

        require(input?.isEmpty()?.not() ?: false){
            "Fail!"
        }

        inputRender = panel(input).rounded()


        return Unit


    }

    fun runApp():Unit{

        HelloDsl().run()
        return Unit
    }

}

fun main(args: Array<String>): Unit {

    val app:HelloDsl = HelloDsl()
    app.runApp()

}

/*
public class HelloDsl extends ToolkitApp {

    @Override
    protected Element render() {
        return panel("Hello",
            text("Welcome to TamboUI DSL!").bold().cyan(),
            spacer(),
            text("Press 'q' to quit").dim()
        ).rounded();
    }

    public void runApp() throws Exception {
        new HelloDsl().run();
    }
}
 */