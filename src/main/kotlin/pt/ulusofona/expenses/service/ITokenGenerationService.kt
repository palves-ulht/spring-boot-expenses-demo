package pt.ulusofona.expenses.service


interface ITokenGenerationService {
    fun generateToken(size: Int = TOKEN_LENGTH): String
}