package thecookingpot.recipe.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import thecookingpot.recipe.dto.UserDto
import thecookingpot.recipe.exception.EmailExistsException
import thecookingpot.recipe.exception.UsernameExistsException
import thecookingpot.recipe.model.User
import thecookingpot.recipe.repository.UserRepository

@Service
class SignupService @Autowired constructor(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder) {
    @Throws(UsernameExistsException::class, EmailExistsException::class)
    fun registerNewUser(userDto: UserDto) {
        if (usernameAlreadyRegistered(userDto.username)) {
            throw UsernameExistsException()
        }

        if (emailAlreadyRegistered(userDto.email)) {
            throw EmailExistsException()
        }

        val user = User().apply {
            username = userDto.username
            email = userDto.email
            password = passwordEncoder.encode(userDto.password)
            newUser = true
        }

        userRepository.save(user)
    }

    private fun usernameAlreadyRegistered(username: String): Boolean {
        return userRepository.findByUsername(username).isPresent
    }

    private fun emailAlreadyRegistered(email: String): Boolean {
        return userRepository.findByEmail(email).isPresent
    }
}