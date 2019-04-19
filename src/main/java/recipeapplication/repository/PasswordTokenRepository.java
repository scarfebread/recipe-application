package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipeapplication.model.PasswordResetToken;
import recipeapplication.model.User;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long>
{
    PasswordResetToken findByToken(String token);

    // TODO this isn't working
    void deleteByUser(User user);
}
