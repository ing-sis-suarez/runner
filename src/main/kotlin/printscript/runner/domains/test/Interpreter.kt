package printscript.runner.domains.test

import cli.Config
import consumer.*
import interpreter.Interpret
import printscript.runner.domains.test.dto.TestResultDTO
import printscript.runner.domains.test.dto.TestResultState
import java.io.ByteArrayInputStream
import java.io.InputStream

class Interpreter {


    fun interpret(code: String, version: String, inputs: List<String>, output: String): TestResultDTO {
        val filteredOutput = output.replace("\n", "")
        return if (version == "1.0" || version == "1.1") {
            val input = stringToInputStream(code)
            val interpreter = initializeInterpreter(input, version)
            val res = runConsumer(interpreter, inputs)
            if (res.message.replace("\n", "") == filteredOutput) {
                TestResultDTO(TestResultState.SUCCESS, res.message)
            } else {
                TestResultDTO(TestResultState.FAILURE, "Expected ${output}, but was ${res.message}")
            }
        } else {
            TestResultDTO(TestResultState.FAILURE, "incompatible version exception")
        }
    }

    private fun stringToInputStream(input: String): InputStream {
        val byteArray = input.toByteArray(Charsets.UTF_8)
        return ByteArrayInputStream(byteArray)
    }

    private fun initializeInterpreter(input: InputStream, version: String): ASTNodeConsumerInterpreter {
        return Interpret(Config().generateASTNproviderInputStream(version, input))
    }

    private fun runConsumer(consumer: ASTNodeConsumerInterpreter, inputs: List<String>): TestResultDTO {
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
                    return TestResultDTO(TestResultState.FAILURE, inter)
                }

                is ConsumerResponseInput -> {
                    inter += "${result.msg}\n"
                    if (lista.hasNext()) {
                        val input = lista.next()
                        consumer.getValue(input)
                    } else {
                        inter += "Input not found Exception"
                        return TestResultDTO(TestResultState.FAILURE, inter)
                    }
                }
            }
            result = consumer.consume()
        }
        return TestResultDTO(TestResultState.SUCCESS, inter)
    }
}

//fun main(){
//    val msg = "let people: string = readInput('algo');\nprintln(people + 10);"
//    val inputs = listOf("hola")
//    val result = Interpreter().interpret(msg, "1.1", inputs)
//    println(result)
//}