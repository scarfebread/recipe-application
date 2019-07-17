package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import recipeapplication.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>
{
}
