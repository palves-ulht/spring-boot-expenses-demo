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
@RequestMapping("/api")
class APIController {

    // this endpoint exists only to facilitate credentials validation
    @GetMapping("/validateCredentials")
    fun validateCredentials(): String = "ok"
}