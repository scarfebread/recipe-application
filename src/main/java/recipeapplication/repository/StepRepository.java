package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import recipeapplication.model.Recipe;
import recipeapplication.model.Step;

public interface StepRepository extends JpaRepository<Step, Long>
{
    void deleteByRecipe(Recipe recipe);
}
