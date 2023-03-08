package pt.ulusofona.expenses.repository;

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.expenses.dao.ExpenseType

interface ExpenseTypeRepository : JpaRepository<ExpenseType, Long> {
    fun findByName(name: String): ExpenseType?
}