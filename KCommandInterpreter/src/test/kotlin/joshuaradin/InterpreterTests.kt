package joshuaradin

import joshuaradin.annotation.CommandTemplate
import joshuaradin.annotation.ParameterTemplate
import joshuaradin.annotation.TemplateConverter
import joshuaradin.core.*
import org.junit.Assert
import org.junit.Test


class InterpreterTests {




    @Test
    fun testTokenize() {


        val intended = arrayOf("-i", "3", "--name", "hello' world", "add", "scene1")
        val string = "-i 3 --name \"hello' world\" add scene1"

        val tokenized = with(CommandInterpreter) {
            string.tokenize()
        }

        println(intended.toList())
        println(tokenized)
        Assert.assertArrayEquals("Checking if strings tokenize properly", intended, tokenized.toTypedArray())
    }

    @Test
    fun testCommandTemplate() {
        @CommandTemplate
        class Add {

            @ParameterTemplate(additionalNames = ["--name"], required = true)
            val name: String? = "Hello World!"
        }



        val convertCommand = TemplateConverter.convertCommand<Add, Any?>()
        Assert.assertArrayEquals(convertCommand.names, arrayOf("Add"))
        val parameter = convertCommand.getParameterForName("--name")
        Assert.assertNotNull("parameter should exist", parameter)
        Assert.assertTrue(parameter!!.required)
    }

    @Test
    fun testBasicInterpreter() {
        val commanderBuilder = CommanderBuilder()
        val p1 = Parameter.createInstance<String>(arrayOf("--name"), 1, true)
        val p2 = Parameter.createInstance<Int>(arrayOf("--int", "-i"), 1, false)
        commanderBuilder.addParameters(p1, p2)
        val commandInterpreter = commanderBuilder.build()

        commandInterpreter.parse("--name hello")
        Assert.assertTrue(commandInterpreter.parsed("--name"))
        Assert.assertEquals(commandInterpreter.getParameterValue("--name"), "hello")

        commandInterpreter.parse("--name hello -i 3")
        Assert.assertTrue(commandInterpreter.parsed("--name"))
        Assert.assertEquals(commandInterpreter.getParameterValue("--name"), "hello")
        Assert.assertEquals(commandInterpreter.getParameterValue<Int>("--int"), 3)
    }

    @Test
    fun testBasicCommand() {
        val commanderBuilder = CommanderBuilder()
        @CommandTemplate
        class Add {

            @ParameterTemplate(additionalNames = ["--name"], required = true)
            val name: String? = "Hello World!"
        }



        val convertCommand = TemplateConverter.convertCommand<Add, Any?>()
        commanderBuilder.addCommand(convertCommand)

        val interpreter = commanderBuilder.build()
        interpreter.parse("Add --name boop")
        Assert.assertTrue("Checking if Add command was parsed", interpreter.parsed("Add"))
    }

    @Test
    fun testArithmeticCommand() {
        val commanderBuilder = CommanderBuilder()
        class Adder : IStringConverter<Int>{
            override fun convert(strs: List<String>): Int {
                return strs[0].toInt() + strs[1].toInt()
            }

        }
        class Adder2 : IMultiParameterConverter<Int>{
            override fun convert(strs: Set<ParameterValue<*>>): Int {
                return strs.first().value as Int
            }
        }

        @CommandTemplate(arity = 2, requiredOptionsConverter = Adder::class, converter = Adder2::class)
        class Add



        val convertCommand = TemplateConverter.convertCommand<Add, Int>()
        commanderBuilder.addCommand(convertCommand)

        val interpreter = commanderBuilder.build()
        interpreter.parse("Add 1 3")
        Assert.assertTrue("Checking if Add command was parsed", interpreter.parsed("Add"))
        Assert.assertEquals(interpreter.getCommandValue("Add"), 4)
    }
}