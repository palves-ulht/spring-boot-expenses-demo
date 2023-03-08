package pt.ulusofona.expenses.controller

import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import pt.ulusofona.expenses.dao.Role
import pt.ulusofona.expenses.dao.User
import pt.ulusofona.expenses.dao.passwordEncoder
import pt.ulusofona.expenses.form.UserForm
import pt.ulusofona.expenses.repository.UserRepository
import pt.ulusofona.expenses.service.EmailService
import pt.ulusofona.expenses.service.ITokenGenerationService
import pt.ulusofona.expenses.service.TokenGenerationService

@Controller
@RequestMapping("/web/user")
class WebUserAdminController(
    val userDetailsManager: UserDetailsManager,
    val userRepository: UserRepository,
    val tokenGenerationService: ITokenGenerationService,
    val emailService: EmailService
) {

    @Value("\${base.url}")
    val baseUrl: String = ""

    @GetMapping("/new")
    fun newUserForm(model: ModelMap): String {
        model["userForm"] = UserForm()
        return "user/new-user-form"
    }

    @PostMapping("/new")
    fun newUser(
        @Valid @ModelAttribute("userForm") userForm: UserForm,
        bindingResult: BindingResult,
        model: ModelMap,
        redirectAttributes: RedirectAttributes
    ): String {

        if (bindingResult.hasErrors()) {
            return "user/new-user-form"
        }

        // TODO: check strong password

        if (userDetailsManager.userExists(userForm.email)) {
            bindingResult.rejectValue("email", "user.alreadyExistent", "Error: There is already a user with that email")
            return "user/new-user-form"
        }

        userDetailsManager.createUser(
            User(firstName = userForm.firstName, email = userForm.email, pass = userForm.password, rolesAsList = listOf(Role.ROLE_USER))
        )

        redirectAttributes.addFlashAttribute("message", "We've sent a verification code to your email")
        return "redirect:/web/user/verify"
    }

    @GetMapping("/verify")
    fun verifyForm(model: ModelMap): String {
        return "user/verify-form"
    }

    @PostMapping("/verify")
    fun verify(
        @RequestParam("code") code: String?,
        model: ModelMap,
        redirectAttributes: RedirectAttributes
    ): String {

        if (code == null) {
            model["error"] = "Invalid code"
            return "user/verify-form"
        }

        val user = userRepository.findByEmailToken(code)
        if (user == null) {
            model["error"] = "Invalid code"
            return "user/verify-form"
        }

        user.emailConfirmed = true
        user.emailToken = null
        userRepository.save(user)

        redirectAttributes.addFlashAttribute("message", "Registration successful. You can now login")
        return "redirect:/login"
    }

    @GetMapping("/resetPassword")
    fun resetPasswordForm(model: ModelMap): String {
        return "user/reset-password-form"
    }

    @PostMapping("/resetPassword")
    fun resetPassword(
        @RequestParam("email") email: String?,
        model: ModelMap,
        redirectAttributes: RedirectAttributes
    ): String {

        if (email == null) {
            model["error"] = "Invalid email"
            return "user/reset-password-form"
        }

        val user = userRepository.findByEmail(email)
        if (user == null) {
            model["error"] = "No one is registered with that email"
            return "user/reset-password-form"
        }

        // generate reset token
        val token = tokenGenerationService.generateToken(10)
        user.resetPasswordToken = token
        userRepository.save(user)

        // send email
        emailService.send(recipient = user.email, subject = "Reset password requested",
            body = "Click this link to reset your password: ${baseUrl}/web/user/changePasswordFromEmail?t=${user.resetPasswordToken}")

        redirectAttributes.addFlashAttribute("message", "An email was sent with a reset password link")
        return "redirect:/login"
    }

    @GetMapping("/changePasswordFromEmail")
    fun changePasswordFromEmail(@RequestParam("t") token: String, model: ModelMap): String {

        val user = userRepository.findByResetPasswordToken(token)
        if (user != null) {
            model["token"] = token
            model["email"] = user.email
            return "user/reset-password-form-from-email"
        }

        throw IllegalArgumentException("Invalid token")
    }

    @PostMapping("/changePasswordFromEmail")
    fun changePasswordFromEmail(
        @RequestParam("token") token: String,
        @RequestParam("password") password: String?,
        model: ModelMap,
        redirectAttributes: RedirectAttributes
    ): String {

        val user = userRepository.findByResetPasswordToken(token)

        if (password == null) {
            model["error"] = "Invalid password"
            model["token"] = token
            model["email"] = user?.email
            return "user/reset-password-form-from-email"
        }


        if (user != null) {
            user.pass = passwordEncoder.encode(password)  // TODO check password validity
            user.resetPasswordToken = null
            userRepository.save(user)

            redirectAttributes.addFlashAttribute("message", "Password has been changed. You can now login with the new password")
            return "redirect:/login"
        }

        throw IllegalArgumentException("Invalid token")
    }
}