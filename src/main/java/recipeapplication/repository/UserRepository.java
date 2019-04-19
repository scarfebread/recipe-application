package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipeapplication.model.User;

public interface UserRepository extends JpaRepository<User, Long>
{
    User findByUsername(String username);
    User findByEmail(String email);
}
