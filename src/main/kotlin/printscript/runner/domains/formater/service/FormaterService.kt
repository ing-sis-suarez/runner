package printscript.runner.domains.formater.service

import printscript.runner.domains.linter.dto.ComplianceState
import printscript.runner.domains.linter.dto.SnippetDTO
import java.util.*

interface FormaterService {

    fun format(code: String): String

    fun updateSnippetContent(token: String, id: UUID, oldSnippet: SnippetDTO, content: String): SnippetDTO
    fun getSnippetContent(token: String, id: UUID): SnippetDTO

    fun updateComplianceStatus(token: String, snippetId: UUID, oldSnippet: SnippetDTO, status: ComplianceState)
}