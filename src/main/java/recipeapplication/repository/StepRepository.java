package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import recipeapplication.model.Recipe;
import recipeapplication.model.Step;
import recipeapplication.model.User;

import java.util.Optional;

public interface StepRepository extends JpaRepository<Step, Long>
{
    void deleteByRecipe(Recipe recipe);
    Optional<Step> findById(Long id);
}
