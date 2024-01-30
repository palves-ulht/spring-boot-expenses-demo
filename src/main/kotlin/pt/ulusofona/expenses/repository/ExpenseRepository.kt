package pt.ulusofona.expenses.repository;

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.expenses.dao.Expense
import pt.ulusofona.expenses.dao.ExpenseType
import pt.ulusofona.expenses.dao.User

interface ExpenseRepository : JpaRepository<Expense, Long> {

    fun findAllByOwner(user: User): List<Expense>
    fun findAllByOwnerAndExpenseType(user: User, expenseType: ExpenseType): List<Expense>
    fun findAllByOwnerAndTitleContainingIgnoreCase(user: User, titlePart: String): List<Expense>
    fun findAllByOwnerAndExpenseTypeAndTitleContainingIgnoreCase(user: User, expenseType: ExpenseType, titlePart: String): List<Expense>
}