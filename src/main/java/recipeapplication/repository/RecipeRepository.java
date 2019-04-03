package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipeapplication.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long>
{

}
