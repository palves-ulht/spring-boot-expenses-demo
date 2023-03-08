package pt.ulusofona.expenses.controller

import jakarta.validation.Valid
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.expenses.dao.Expense
import pt.ulusofona.expenses.dao.User
import pt.ulusofona.expenses.form.ExpenseForm
import pt.ulusofona.expenses.repository.ExpenseRepository
import pt.ulusofona.expenses.repository.ExpenseTypeRepository

@Controller
@RequestMapping("/web/expense")
class WebExpenseController(
    val expenseRepository: ExpenseRepository,
    val expenseTypeRepository: ExpenseTypeRepository) {

    @GetMapping("/new")
    fun newExpenseForm(model: ModelMap): String {
        model["expenseForm"] = ExpenseForm()
        model["expenseTypes"] = expenseTypeRepository.findAll()
        return "new-expense-form"
    }

    @PostMapping("/new")
    fun newExpense(@Valid @ModelAttribute("expenseForm") expenseForm: ExpenseForm,
                   bindingResult: BindingResult,
                   model: ModelMap,
                   redirectAttributes: RedirectAttributes,
                   authentication: Authentication
    ): String {

        val expenseTypeNames = expenseTypeRepository.findAll()
        model["expenseTypes"] = expenseTypeNames

        if (bindingResult.hasErrors()) {
            return "new-expense-form"
        }

        if (expenseForm.type !in expenseTypeNames.map { it.id }) {
            bindingResult.rejectValue("type", "expenseType.invalidValue",
                "Error: You must choose a valid type")
        }

        val user = authentication.principal as User
        val expenseType = expenseTypeRepository.findByIdOrNull(expenseForm.type!!)
        val expense = Expense(title = expenseForm.title!!, expenseType = expenseType, amount = expenseForm.amount!!, owner = user)
        expenseRepository.save(expense)

        return "redirect:/web/expense/list"
    }

    @GetMapping("/list")
    fun listExpenses(model: ModelMap, authentication: Authentication): String {

        val user = authentication.principal as User

        model["expenses"] = expenseRepository.findAllByOwner(user)
        return "list-expenses"
    }
}