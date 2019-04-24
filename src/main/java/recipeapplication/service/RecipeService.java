package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.CreateRecipeDto;
import recipeapplication.dto.RecipeDto;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.model.Recipe;
import recipeapplication.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

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

    public void createRecipe(CreateRecipeDto createRecipeDto)
    {
        Recipe recipe = new Recipe();

        recipe.setTitle(createRecipeDto.getTitle());

        recipeRepository.save(recipe);
    }

    public List<Recipe> getRecipes()
    {
        return recipeRepository.findAll();
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
