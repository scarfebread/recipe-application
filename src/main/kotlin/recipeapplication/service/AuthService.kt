package recipeapplication.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolder.getContext
import org.springframework.stereotype.Service
import recipeapplication.model.User
import recipeapplication.security.AuthenticationPrincipal

@Service
class AuthService {
    val loggedInUser: User
        get() = (getContext().authentication.principal as AuthenticationPrincipal).user

    fun disablePasswordReset() {
        val user = getContext().authentication.principal as AuthenticationPrincipal
//        if (user.changePasswordAccess) {
//            user.changePasswordAccess = false
//            getContext().authentication = UsernamePasswordAuthenticationToken(user, null)
//        }
    }

    fun disableUserSession() {
        SecurityContextHolder.clearContext()
    }
}