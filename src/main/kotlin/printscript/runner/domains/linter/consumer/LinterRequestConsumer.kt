package printscript.runner.domains.linter.consumer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import printscript.runner.domains.linter.service.LinterService
import spring.mvc.redis.streams.RedisStreamConsumer
import java.time.Duration
import java.util.*

@Component
class SampleLinterConsumer @Autowired constructor(
    redis: RedisTemplate<String, String>,
    @Value("\${redis.stream.request_linter_key}") streamKey: String,
    @Value("\${redis.groups.lint}") groupId: String,
) : RedisStreamConsumer<LintRequestEvent>(streamKey, groupId, redis) {

    @Autowired
    private lateinit var linterService: LinterService


    constructor(
        redis: RedisTemplate<String, String>,
        @Value("\${redis.stream.request_key}") streamKey: String,
        @Value("\${redis.groups.lint}") groupId: String,
        linterService: LinterService
    ) : this(redis, streamKey, groupId) {
        this.linterService = linterService
    }

    init {
        subscription()
    }

    override fun onMessage(record: ObjectRecord<String, LintRequestEvent>) {
        println(record.value.lintRulesId)
        val (snippetId, lintRulesId, token) = record.value
        val rules = linterService.getRules(token, lintRulesId)
        val snippetDTO = linterService.getSnippetContent(token, snippetId)
        val lintResult = linterService.lint(snippetDTO.content, rules)
        linterService.updateComplianceStatus(token, snippetId, snippetDTO, lintResult.compliance)
    }

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LintRequestEvent>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
            .targetType(LintRequestEvent::class.java) // Set type to de-serialize record
            .build()
    }
}

data class LintRequestEvent(
    val snippetId: UUID,
    val lintRulesId: UUID,
    val token: String
)