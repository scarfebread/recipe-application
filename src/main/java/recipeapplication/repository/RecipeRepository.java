package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipeapplication.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long>
{
    Optional<Recipe> findByIdAndUserId(Long id, Long userId);

    List<Recipe> findByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
