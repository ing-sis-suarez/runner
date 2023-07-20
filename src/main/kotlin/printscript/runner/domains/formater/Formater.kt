package printscript.runner.domains.formater

import cli.Config
import config.FormatterConfig
import consumer.*
import formatter.RegularFormatter
import java.io.ByteArrayInputStream
import java.io.InputStream

class Formater {

    fun format(code: String): String {
        val input = stringToInputStream(code)
        val formater = initializeFormater(input)
        return runConsumer(formater)
    }

    private fun stringToInputStream(input: String): InputStream {
        val byteArray = input.toByteArray(Charsets.UTF_8)
        return ByteArrayInputStream(byteArray)
    }

    private fun initializeFormater(input: InputStream): ASTNodeConsumer {
        return RegularFormatter(Config().generateASTNproviderInputStream("1.1", input), FormatterConfig())
    }

    private fun runConsumer(consumer: ASTNodeConsumer): String {
        var result = consumer.consume()
        var formated = ""
        while (result !is ConsumerResponseEnd) {
            when (result) {
                is ConsumerResponseSuccess -> {
                    formated += "${result.msg}\n"
                }
            }
            result = consumer.consume()
        }
        return formated
    }
}

