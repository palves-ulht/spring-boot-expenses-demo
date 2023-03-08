package pt.ulusofona.expenses.dao

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class ExpenseType(
    @Id @GeneratedValue
    val id: Long = 0,

    val name: String = ""
)
