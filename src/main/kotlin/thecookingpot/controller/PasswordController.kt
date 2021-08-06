package thecookingpot.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thecookingpot.dto.PasswordDto
import thecookingpot.dto.PasswordResetDto
import thecookingpot.exception.AuthServerException
import thecookingpot.exception.UserNotFoundException
import thecookingpot.service.AuthService
import thecookingpot.service.UserService
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

        try {
            userService.changePassword(authService.loggedInUser, passwordDto.password)
        } catch (e: AuthServerException) {
            return ResponseEntity.status(500).body("Unable to contact the login server")
        }

        return ResponseEntity.status(201).body("Created")
    }
}