package printscript.runner.domains.test.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import printscript.runner.domains.test.dto.TestRequestDTO
import printscript.runner.domains.test.dto.TestResultDTO
import printscript.runner.domains.test.service.TestService
import java.security.Principal


@RestController
@CrossOrigin("*")
class TestController {

    @Autowired
    private var testService: TestService

    @Autowired
    constructor(testService: TestService) {
        this.testService = testService
    }

    @PostMapping("/test")
    fun runTest(
        @RequestHeader("Authorization") token: String,
        @RequestBody testDTO: TestRequestDTO,
        principal: Principal
    ): ResponseEntity<TestResultDTO> {
        return ResponseEntity(testService.runTest(testDTO), org.springframework.http.HttpStatus.OK)
    }
}