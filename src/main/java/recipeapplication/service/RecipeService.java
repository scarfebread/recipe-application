package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.CreateRecipeDto;
import recipeapplication.dto.RecipeDto;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.model.Ingredient;
import recipeapplication.model.Recipe;
import recipeapplication.model.User;
import recipeapplication.repository.IngredientRepository;
import recipeapplication.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.sun.org.apache.regexp.internal.RECompiler;

@Service
@Transactional
public class RecipeService
{
    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;
    private AuthService authService;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, AuthService authService)
    {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
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

        // TODO this seems a bad way of managing the one to many relationship
        ingredientRepository.deleteByRecipe(recipe);
        List<Ingredient> ingredients = new ArrayList<>();
        for (String ingredient : recipeDto.getIngredients())
        {
            ingredients.add(new Ingredient(recipe, ingredient));
        }

        recipe.setIngredients(ingredients);

        recipeRepository.save(recipe);
    }

    public void shareRecipe(RecipeDto recipeDto, User user) throws RecipeDoesNotExistException
    {
        Recipe recipe = getRecipe(recipeDto.getId());

        User loggedInUser = authService.getLoggedInUser();

        Recipe sharedRecipe = new Recipe();

        // TODO there should be a better way of cloning a recipe
        sharedRecipe.setTitle(recipe.getTitle());
        sharedRecipe.setDifficulty(recipe.getDifficulty());
        sharedRecipe.setPrepTime(recipe.getPrepTime());
        sharedRecipe.setCookTime(recipe.getCookTime());
        sharedRecipe.setServes(recipe.getServes());
        sharedRecipe.setNotes(recipe.getNotes());
        sharedRecipe.setSharedBy(loggedInUser.getUsername());
        sharedRecipe.setUserId(user.getId());
        sharedRecipe.setRating(recipe.getRating());
        sharedRecipe.setIngredients(new ArrayList<>(recipe.getIngredients()));
        sharedRecipe.setSteps(new ArrayList<>());

        recipeRepository.save(sharedRecipe);
    }
}
