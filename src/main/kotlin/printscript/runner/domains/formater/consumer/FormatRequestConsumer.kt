package printscript.runner.domains.formater.consumer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import printscript.runner.domains.formater.service.FormaterService
import spring.mvc.redis.streams.RedisStreamConsumer
import java.time.Duration
import java.util.*

@Component
class SampleFormaterConsumer @Autowired constructor(
    redis: RedisTemplate<String, String>,
    @Value("\${redis.stream.request_formater_key}") streamKey: String,
    @Value("\${redis.groups.format}") groupId: String,
) : RedisStreamConsumer<FormatRequestEvent>(streamKey, groupId, redis) {

    @Autowired
    private lateinit var formaterService: FormaterService


    constructor(
        redis: RedisTemplate<String, String>,
        @Value("\${redis.stream.request_formater_key}") streamKey: String,
        @Value("\${redis.groups.format}") groupId: String,
        formaterService: FormaterService
    ) : this(redis, streamKey, groupId) {
        this.formaterService = formaterService
    }

    init {
        subscription()
    }

    override fun onMessage(record: ObjectRecord<String, FormatRequestEvent>) {
        val (snippetId, formatRulesId, token) = record.value
        val snippetDTO = formaterService.getSnippetContent(token, snippetId)
        val formatResult = formaterService.format(snippetDTO.content)
        formaterService.updateSnippetContent(token, snippetId, snippetDTO, formatResult)
    }

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, FormatRequestEvent>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
            .targetType(FormatRequestEvent::class.java) // Set type to de-serialize record
            .build()
    }
}

data class FormatRequestEvent(
    val snippetId: UUID,
    val formatRulesId: UUID,
    val token: String
)