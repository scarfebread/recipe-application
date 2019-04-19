package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipeapplication.model.PasswordResetToken;
import recipeapplication.model.User;

import javax.transaction.Transactional;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long>
{
    PasswordResetToken findByToken(String token);
    
    @Transactional
    void deleteByUser(User user);
}
