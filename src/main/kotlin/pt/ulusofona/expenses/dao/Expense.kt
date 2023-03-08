package pt.ulusofona.expenses.dao

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*


@Entity
class Expense(
    @Id @GeneratedValue
    val id: Long = 0,

    val title: String = "",

    @ManyToOne(optional = false)
    @JoinColumn(name = "expense_type_id", nullable = false)
    val expenseType: ExpenseType? = null,

    val amount: Double = 0.0,

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    val owner: User? = null
)
