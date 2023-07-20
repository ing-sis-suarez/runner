package printscript.runner.domains.test.service

import printscript.runner.domains.test.dto.TestRequestDTO
import printscript.runner.domains.test.dto.TestResultDTO

interface TestService {
    fun runTest(dto: TestRequestDTO): TestResultDTO
}