package thecookingpot.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thecookingpot.dto.UserDto
import thecookingpot.exception.EmailExistsException
import thecookingpot.exception.UsernameExistsException
import thecookingpot.service.SignupService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/api/signup"])
class SignupController @Autowired constructor(private val signupService: SignupService) {
    @PostMapping
    fun signup(@RequestBody userDto: @Valid UserDto, errors: Errors): ResponseEntity<*> {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body("Invalid user information supplied")
        }

        try {
            signupService.registerNewUser(userDto)
        } catch (usernameExists: UsernameExistsException) {
            return ResponseEntity.status(400).body("Username already exists")
        } catch (emailExists: EmailExistsException) {
            return ResponseEntity.status(400).body("Email address already exists")
        }

        return ResponseEntity.status(201).body("Created")
    }
}