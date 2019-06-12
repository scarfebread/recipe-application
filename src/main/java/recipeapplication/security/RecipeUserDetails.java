package recipeapplication.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import recipeapplication.model.User;

import java.util.Collection;
import java.util.Collections;

public class RecipeUserDetails implements UserDetails
{
    private User user;
    private boolean changePasswordAccess;

    public RecipeUserDetails(User user)
    {
        this.user = user;
        changePasswordAccess = false;
    }

    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        if (!changePasswordAccess)
        {
            return Collections.singletonList(new SimpleGrantedAuthority(Role.USER.toString()));
        }

        return null;
    }

    public String getUsername()
    {
        return user.getUsername();
    }

    public String getPassword()
    {
        return user.getPassword();
    }

    public User getUser()
    {
        return user;
    }

    public boolean isAccountNonExpired()
    {
        return true;
    }

    public boolean isAccountNonLocked()
    {
        return true;
    }

    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    public boolean isEnabled()
    {
        return true;
    }

    public void setChangePasswordAccess(boolean changePasswordAccess)
    {
        this.changePasswordAccess = changePasswordAccess;
    }

    public boolean isChangePasswordAccess()
    {
        return changePasswordAccess;
    }
}
