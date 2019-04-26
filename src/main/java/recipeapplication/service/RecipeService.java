package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.CreateRecipeDto;
import recipeapplication.dto.RecipeDto;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.model.Recipe;
import recipeapplication.model.User;
import recipeapplication.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

import com.sun.org.apache.regexp.internal.RECompiler;

@Service
public class RecipeService
{
    private RecipeRepository recipeRepository;
    private AuthService authService;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, AuthService authService)
    {
        this.recipeRepository = recipeRepository;
        this.authService = authService;
    }

    public Recipe createRecipe(CreateRecipeDto createRecipeDto)
    {
        User user = authService.getLoggedInUser();

        Recipe recipe = new Recipe();

        recipe.setTitle(createRecipeDto.getTitle());
        recipe.setUserId(user.getId());
        recipe.setRating(0L);

        return recipeRepository.save(recipe);
    }

    public List<Recipe> getRecipes()
    {
        User user = authService.getLoggedInUser();

        return recipeRepository.findByUserId(user.getId());
    }

    public void deleteRecipe(RecipeDto recipeDto) throws RecipeDoesNotExistException
    {
        Optional<Recipe> result = recipeRepository.findByIdAndUserId(
                recipeDto.getId(),
                authService.getLoggedInUser().getId()
        );

        if (!result.isPresent())
        {
            throw new RecipeDoesNotExistException();
        }

        recipeRepository.deleteById(recipeDto.getId());
    }
}
