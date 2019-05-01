package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import recipeapplication.model.Ingredient;
import recipeapplication.model.Recipe;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>
{
    void deleteByRecipe(Recipe recipe);
}
