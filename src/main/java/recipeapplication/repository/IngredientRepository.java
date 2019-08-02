package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import recipeapplication.model.Ingredient;
import recipeapplication.model.User;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>
{
    @Query("SELECT DISTINCT description FROM Ingredient WHERE user_id = ?1")
    List<String> getIngredients(User user);

    Optional<Ingredient> findByIdAndUser(Long id, User user);
}
