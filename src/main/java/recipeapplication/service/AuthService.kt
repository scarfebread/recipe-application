package recipeapplication.service

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import recipeapplication.model.User
import recipeapplication.security.RecipeUserDetails
import recipeapplication.security.Role

@Service
class AuthService {
    val loggedInUser: User
        get() = (SecurityContextHolder.getContext().authentication.principal as RecipeUserDetails).user

    fun disablePasswordReset() {
        val user = SecurityContextHolder.getContext().authentication.principal as RecipeUserDetails
        if (user.changePasswordAccess) {
            user.changePasswordAccess = false
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(user, null)
        }
    }

    fun authenticateUser(userDetails: RecipeUserDetails, role: Role) {
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                listOf(SimpleGrantedAuthority(role.toString()))
        )
    }

    fun disableUserSession() {
        SecurityContextHolder.clearContext()
    }
}