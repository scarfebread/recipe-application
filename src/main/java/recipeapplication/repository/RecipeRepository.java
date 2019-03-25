package recipeapplication.repository;

import org.springframework.data.repository.CrudRepository;
import recipeapplication.model.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long>
{

}
