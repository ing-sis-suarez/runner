package printscript.runner.service

import cli.Config
import consumer.*
import interpreter.Interpret
import java.io.ByteArrayInputStream
import java.io.InputStream

class Interpreter{
    fun interpret(code: String, version: String,inputs: List<String>): String {
        val input = stringToInputStream(code)
        val interpreter = initializeInterpreter(input, version)
        return runConsumer(interpreter, inputs)
    }

    private fun stringToInputStream(input: String): InputStream {
        val byteArray = input.toByteArray(Charsets.UTF_8)
        return ByteArrayInputStream(byteArray)
    }

    private fun initializeInterpreter(input: InputStream, version: String): ASTNodeConsumerInterpreter {
        return Interpret(Config().generateASTNproviderInputStream(version, input))
    }

    private fun runConsumer(consumer: ASTNodeConsumerInterpreter, inputs: List<String>): String {
        var result = consumer.consume()
        var inter = ""
        val lista = inputs.iterator()
        while (result !is ConsumerResponseEnd) {
            when (result) {
                is ConsumerResponseSuccess -> {
                    if (result.msg != null) {
                        inter += "${result.msg}\n"
                    }
                }
                is ConsumerResponseError -> {
                    inter += "${result.error}\n"
                    return inter
                }
                is ConsumerResponseImput -> {
                    inter += "${result.msg}\n"
                    if (lista.hasNext()){
                        val input = lista.next()
                        consumer.getValue(input)
                    } else  {
                        inter += "Input not found Exception"
                        return inter
                    }
                }
            }
            result = consumer.consume()
        }
        return inter
    }
}

//fun main(){
//    val msg = "let people: string = readInput('algo');\nprintln(people + 10);"
//    val inputs = listOf("hola")
//    val result = Interpreter().interpret(msg, "1.1", inputs)
//    println(result)
//}