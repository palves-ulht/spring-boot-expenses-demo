package pt.ulusofona.expenses

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pt.ulusofona.expenses.filters.ControllerRequestsLoggingFilter


@Configuration
class ControllerRequestsLoggingFilterConfig {

    // ********** IMPORTANT **********
    // don't forget to include the following line in application.properties:
    // logging.level.org.springframework.web.filter.ControllerRequestsLoggingFilter=INFO

    @Bean
    fun logFilter() = ControllerRequestsLoggingFilter()
}
