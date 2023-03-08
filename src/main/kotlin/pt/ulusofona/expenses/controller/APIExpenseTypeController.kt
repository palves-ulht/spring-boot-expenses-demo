package pt.ulusofona.expenses.controller

import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.ulusofona.expenses.dao.ExpenseType
import pt.ulusofona.expenses.repository.ExpenseTypeRepository

@RestController
@RequestMapping("/api/expenseType")
class APIExpenseTypeController(val expenseTypeRepository: ExpenseTypeRepository) {

    @GetMapping("/list")
    fun list(model: ModelMap): List<ExpenseType> = expenseTypeRepository.findAll()
}