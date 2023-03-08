package pt.ulusofona.expenses.repository;

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.expenses.dao.Expense
import pt.ulusofona.expenses.dao.User

interface ExpenseRepository : JpaRepository<Expense, Long> {

    fun findAllByOwner(user: User): List<Expense>
}