package printscript.runner.domains.test.service

import org.springframework.stereotype.Service
import printscript.runner.domains.test.Interpreter
import printscript.runner.domains.test.dto.TestRequestDTO
import printscript.runner.domains.test.dto.TestResultDTO


@Service
class TestServiceImpl: TestService {
    override fun runTest(dto: TestRequestDTO): TestResultDTO {
        return Interpreter().interpret(dto.content, dto.version, dto.inputs, dto.output)
    }
}