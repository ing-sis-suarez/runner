package printscript.runner.domains.linter.service

import printscript.runner.domains.linter.dto.ComplianceState
import printscript.runner.domains.linter.dto.LintResultDTO
import printscript.runner.domains.linter.dto.LinterRulesDTO
import printscript.runner.domains.linter.dto.SnippetDTO
import java.util.*

interface LinterService {

    fun lint(code: String, rules: LinterRulesDTO): LintResultDTO

    fun getRules(token: String, resourceId: UUID): LinterRulesDTO
    fun getSnippetContent(token: String, id: UUID): SnippetDTO

    fun updateComplianceStatus(token: String, snippetId: UUID, oldSnippet: SnippetDTO, status: ComplianceState)
}