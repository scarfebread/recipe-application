package thecookingpot.auth.security

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import thecookingpot.recipe.model.User

class AuthenticationPrincipal(val user: User, oidcUser: OidcUser) :
    DefaultOidcUser(oidcUser.authorities, oidcUser.idToken, oidcUser.userInfo)