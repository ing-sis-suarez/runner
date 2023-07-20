package printscript.runner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class RunnerApplication

fun main(args: Array<String>) {
    runApplication<RunnerApplication>(*args)
}
