package printscript.runner.domains.linter.dto

import com.fasterxml.jackson.annotation.JsonValue

data class LinterRulesDTO(val caseConvention: CaseConvention, val printExpressionsEnabled: Boolean)

data class LintResultDTO(val compliance: ComplianceState, val errors: List<String>)


enum class CaseConvention(@get:JsonValue val value: String) {
    CAMEL_CASE("camel_case"),
    SNAKE_CASE("snake_case")
}

enum class ComplianceState(@get:JsonValue val value: String) {
    COMPLIANT("COMPLIANT"),
    NON_COMPLIANT("NON_COMPLIANT"),
    PENDING("PENDING")
}