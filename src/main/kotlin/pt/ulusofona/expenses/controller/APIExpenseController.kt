package pt.ulusofona.expenses.controller

import jakarta.validation.Valid
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.ui.ModelMap
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import pt.ulusofona.expenses.dao.Expense
import pt.ulusofona.expenses.dao.User
import pt.ulusofona.expenses.form.ExpenseForm
import pt.ulusofona.expenses.repository.ExpenseRepository
import pt.ulusofona.expenses.repository.ExpenseTypeRepository


@RestController
@RequestMapping("/api/expense")
class APIExpenseController(val expenseRepository: ExpenseRepository,
                           val expenseTypeRepository: ExpenseTypeRepository
) {

    // this handler in needed for responding the actual error message when there is a validation error
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<String?>? {
        val errorMessage = ex.bindingResult.fieldError!!.defaultMessage
        return ResponseEntity.badRequest().body(errorMessage)
    }

    @GetMapping("/list", produces = ["application/json;charset=UTF-8"])
    fun list(model: ModelMap, authentication: Authentication): List<Expense> =
        expenseRepository.findAllByOwner(authentication.principal as User)

    @PostMapping("/new")
    @ResponseBody
    fun newExpense(
        @Valid @RequestBody expenseForm: ExpenseForm,
        authentication: Authentication
    ): ResponseEntity<Map<String, Long>> {

        val user = authentication.principal as User
        val expenseType = expenseTypeRepository.findByIdOrNull(expenseForm.type!!)
        val expense = Expense(title = expenseForm.title!!, expenseType = expenseType, amount = expenseForm.amount!!, owner = user)
        expenseRepository.save(expense)

        return ResponseEntity.status(HttpStatus.CREATED).body(mapOf("id" to expense.id))
    }
}