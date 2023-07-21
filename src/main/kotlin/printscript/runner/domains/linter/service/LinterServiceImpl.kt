package printscript.runner.domains.linter.service

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import printscript.runner.domains.linter.Linter
import printscript.runner.domains.linter.dto.*
import java.util.*

@Service
class LinterServiceImpl : LinterService {

    val linter = Linter()
    override fun lint(code: String, rules: LinterRulesDTO): LintResultDTO {
        return linter.lint(code, rules)
    }

    override fun getRules(token: String, resourceId: UUID): LinterRulesDTO {
        val url = System.getenv("SNIPPET_MANAGER_URI") + "/linter_rules"
        val template = RestTemplate()
        val headers = HttpHeaders()
        prepareHeaders(headers, token)
        val requestEntity = HttpEntity<Void>(headers)
        return template.exchange(url, HttpMethod.GET, requestEntity, LinterRulesDTO::class.java).body!!
    }

    override fun getSnippetContent(token: String, id: UUID): SnippetDTO {
        val url = System.getenv("SNIPPET_MANAGER_URI") + "/snippet/$id"
        val template = RestTemplate()
        val headers = HttpHeaders()
        prepareHeaders(headers, token)
        val requestEntity = HttpEntity<Void>(headers)
        return template.exchange(url, HttpMethod.GET, requestEntity, SnippetDTO::class.java).body!!
    }

    override fun updateComplianceStatus(
        token: String,
        snippetId: UUID,
        oldSnippet: SnippetDTO,
        status: ComplianceState
    ) {
        val url = System.getenv("SNIPPET_MANAGER_URI") + "/snippet/$snippetId"
        val template = RestTemplate()
        val headers = HttpHeaders()
        prepareHeaders(headers, token)
        val body = UpdateSnippetDTO(oldSnippet.title, oldSnippet.content, oldSnippet.type, status)
        val requestEntity = HttpEntity<UpdateSnippetDTO>(body, headers)
        template.exchange(url, HttpMethod.PUT, requestEntity, SnippetDTO::class.java)
    }

    private fun prepareHeaders(headers: HttpHeaders, token: String) {
        headers.set("Content-Type", "application/json")
        headers.set("Authorization", token)
    }

}