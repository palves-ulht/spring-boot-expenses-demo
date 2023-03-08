package pt.ulusofona.expenses

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExpensesDemoApplication

fun main(args: Array<String>) {
    runApplication<ExpensesDemoApplication>(*args)
}
