package thecookingpot.recipe.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thecookingpot.recipe.dto.PasswordDto
import thecookingpot.recipe.dto.PasswordResetDto
import thecookingpot.recipe.exception.UserNotFoundException
import thecookingpot.recipe.service.AuthService
import thecookingpot.recipe.service.UserService
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class PasswordController @Autowired constructor(private val userService: UserService, private val authService: AuthService) {
    @PostMapping("/password-reset")
    fun createPasswordReset(request: HttpServletRequest, @RequestBody passwordResetDto: PasswordResetDto): ResponseEntity<*> {
        val serverName = "${request.serverName}:${request.serverPort}"

        try {
            userService.createPasswordResetToken(serverName, passwordResetDto)
        } catch (e: UserNotFoundException) {
            // We don't show the client that the user doesn't exist for security reasons
        }

        return ResponseEntity.status(201).body("Created")
    }

    @PostMapping("/change-password")
    fun changePassword(@RequestBody passwordDto: @Valid PasswordDto, errors: Errors): ResponseEntity<*> {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body("Invalid password")
        }

        userService.changePassword(authService.loggedInUser, passwordDto.password)

        return ResponseEntity.status(201).body("Created")
    }
}