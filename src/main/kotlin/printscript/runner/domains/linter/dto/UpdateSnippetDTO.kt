package printscript.runner.domains.linter.dto

import printscript.runner.domains.linter.dto.ComplianceState

data class UpdateSnippetDTO(val name: String?, val content: String?, val type: String?, val compliance: ComplianceState?)