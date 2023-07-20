package printscript.runner.domains.formater.service

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import printscript.runner.domains.formater.Formater
import printscript.runner.domains.linter.dto.ComplianceState
import printscript.runner.domains.linter.dto.SnippetDTO
import printscript.runner.domains.linter.dto.UpdateSnippetDTO
import java.util.*

@Service
class FormaterServiceImpl : FormaterService {

    val formater = Formater()
    override fun format(code: String): String {
        return formater.format(code)
    }

    override fun updateSnippetContent(token: String, id: UUID, oldSnippet: SnippetDTO, content: String): SnippetDTO {
        val url = System.getenv("SNIPPET_MANAGER_URI") + "/snippet/$id"
        val template = RestTemplate()
        val headers = HttpHeaders()
        prepareHeaders(headers, token)
        val body = UpdateSnippetDTO(oldSnippet.title, content, oldSnippet.type, oldSnippet.compliance)
        val requestEntity = HttpEntity<UpdateSnippetDTO>(body, headers)
        return template.exchange(url, HttpMethod.PUT, requestEntity, SnippetDTO::class.java).body!!
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