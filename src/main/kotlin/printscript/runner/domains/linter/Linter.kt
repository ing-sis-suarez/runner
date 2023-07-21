package printscript.runner.domains.linter

import StaticCodeAnalyzer
import cli.Config
import consumer.ASTNodeConsumer
import consumer.ConsumerResponseEnd
import consumer.ConsumerResponseError
import printscript.runner.domains.linter.dto.CaseConvention
import printscript.runner.domains.linter.dto.ComplianceState
import printscript.runner.domains.linter.dto.LintResultDTO
import printscript.runner.domains.linter.dto.LinterRulesDTO
import java.io.ByteArrayInputStream
import java.io.InputStream

class Linter {

    fun lint(code: String, dto: LinterRulesDTO): LintResultDTO {
        val input = stringToInputStream(code)
        val sCA = initializeSCA(input, dto)
        return runConsumer(sCA)

    }

    private fun stringToInputStream(input: String): InputStream {
        val byteArray = input.toByteArray(Charsets.UTF_8)
        return ByteArrayInputStream(byteArray)
    }

    private fun initializeSCA(input: InputStream, config: LinterRulesDTO): ASTNodeConsumer {
        val case = when(config.caseConvention){
            CaseConvention.CAMEL_CASE -> "CamelCaseFormat"
            CaseConvention.SNAKE_CASE -> "SnakeCaseFormat"
        }
        val map = mutableMapOf(
            case to true,
            "MethodNoExpresion" to !config.printExpressionsEnabled,
            "InputNoExpresion" to !config.printExpressionsEnabled
            )

        return StaticCodeAnalyzer(map, Config().generateASTNproviderInputStream("1.1", input))
    }

    private fun runConsumer(consumer: ASTNodeConsumer): LintResultDTO{
        val errors: MutableList<String> = mutableListOf()
        var state: ComplianceState = ComplianceState.COMPLIANT
        var result = consumer.consume()
        while (result !is ConsumerResponseEnd) {
            when (result) {
                is ConsumerResponseError -> {
                    state = ComplianceState.NON_COMPLIANT
                    errors.add(result.error)
                }
            }
            result = consumer.consume()
        }
        return LintResultDTO(state, errors)
    }
}

//fun main(){
//    val dto = LinterRulesDTO(CaseConvention.SNAKE_CASE, true)
//    val msg = "let camelCase: number = 2;\n print(camel_case + 'algo');"
//    val result = Linter().lint(msg, dto)
//    println(result)
//}