package thecookingpot.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import thecookingpot.model.User

class RecipeUserDetails(val user: User) : UserDetails {
    var changePasswordAccess = false

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return if (!changePasswordAccess) {
            listOf(SimpleGrantedAuthority(Role.USER.toString()))
        } else emptyList() // TODO I need to validate this is ok
    }

    override fun getUsername(): String {
        return user.username
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}