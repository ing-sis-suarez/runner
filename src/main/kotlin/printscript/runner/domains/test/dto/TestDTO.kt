package printscript.runner.domains.test.dto

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

data class TestResultDTO(val state: TestResultState, val message: String)

data class TestRequestDTO(
    val id: UUID,
    val language: String,
    val content: String,
    val version: String,
    val inputs: List<String>,
    val output: String,
)
enum class TestResultState(@get: JsonValue val state: String) {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE")
}