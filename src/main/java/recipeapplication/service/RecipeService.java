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
        recipe.setServes(1L);
        recipe.setDifficulty("Medium");

        return recipeRepository.save(recipe);
    }

    public List<Recipe> getRecipes()
    {
        User user = authService.getLoggedInUser();

        return recipeRepository.findByUserId(user.getId());
    }

    public Recipe getRecipe(Long id) throws RecipeDoesNotExistException
    {
        Optional<Recipe> result = recipeRepository.findByIdAndUserId(id, authService.getLoggedInUser().getId());

        if (!result.isPresent())
        {
            throw new RecipeDoesNotExistException();
        }

        return result.get();
    }

    public void deleteRecipe(RecipeDto recipeDto) throws RecipeDoesNotExistException
    {
        // Exception will be thrown if it does not exist
        getRecipe(recipeDto.getId());

        recipeRepository.deleteById(recipeDto.getId());
    }

    public void updateRecipe(RecipeDto recipeDto) throws RecipeDoesNotExistException
    {
        // Exception will be thrown if it does not exist
        Recipe recipe = getRecipe(recipeDto.getId());

        recipe.setNotes(recipeDto.getNotes());
        recipe.setRating(recipeDto.getRating());
        recipe.setServes(recipeDto.getServes());
        recipe.setCookTime(recipeDto.getCookTime());
        recipe.setPrepTime(recipeDto.getPrepTime());
        recipe.setDifficulty(recipeDto.getDifficulty());

        recipeRepository.save(recipe);
    }
}
