package printscript.runner.domains.linter.dto

import printscript.runner.domains.linter.dto.ComplianceState
import java.util.*

data class SnippetDTO(val id: UUID, val title: String, val content: String, val createdAt: Date, val type: String, val compliance: ComplianceState)