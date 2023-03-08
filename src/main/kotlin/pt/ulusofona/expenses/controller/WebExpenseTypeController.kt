package pt.ulusofona.expenses.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import pt.ulusofona.expenses.repository.ExpenseTypeRepository

@Controller
@RequestMapping("/web/expenseType")
class WebExpenseTypeController(val expenseTypeRepository: ExpenseTypeRepository) {

    @GetMapping("/list")
    fun list(model: ModelMap): String {
        model["expenseTypes"] = expenseTypeRepository.findAll()
        return "list-expense-types"
    }
}