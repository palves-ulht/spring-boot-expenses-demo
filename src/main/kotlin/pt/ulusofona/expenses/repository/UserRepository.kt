package pt.ulusofona.expenses.repository;

import org.springframework.data.jpa.repository.JpaRepository
import pt.ulusofona.expenses.dao.User

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun existsByEmailToken(emailToken: String): Boolean
    fun findByEmailToken(emailToken: String): User?
    fun findByResetPasswordToken(resetPasswordToken: String): User?
}