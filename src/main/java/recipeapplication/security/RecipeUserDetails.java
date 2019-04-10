package recipeapplication.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import recipeapplication.model.User;

import java.util.Collection;

public class RecipeUserDetails implements UserDetails
{
    private User user;

    public RecipeUserDetails(User user)
    {
        this.user = user;
    }

    public Collection<? extends GrantedAuthority> getAuthorities()
    {
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
}
