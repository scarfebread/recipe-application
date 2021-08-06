package thecookingpot.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import thecookingpot.repository.UserRepository
import thecookingpot.security.RecipeUserDetails

@Service
class RecipeUserDetailsService @Autowired constructor(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return RecipeUserDetails(
                userRepository.findByUsername(username).orElseThrow { UsernameNotFoundException(username) }
        )
    }
}