package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.CreateRecipeDto;
import recipeapplication.dto.RecipeDto;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.model.*;
import recipeapplication.repository.IngredientRepository;
import recipeapplication.repository.RecentlyViewedRepository;
import recipeapplication.repository.RecipeRepository;
import recipeapplication.repository.StepRepository;
import recipeapplication.utility.RecipeTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
@Transactional
public class RecipeService
{
    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;
    private StepRepository stepRepository;
    private AuthService authService;
    private RecentlyViewedRepository recentlyViewedRepository;

    @Autowired
    public RecipeService(
            RecipeRepository recipeRepository,
            IngredientRepository ingredientRepository,
            StepRepository stepRepository,
            AuthService authService,
            RecentlyViewedRepository recentlyViewedRepository)
    {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.stepRepository = stepRepository;
        this.authService = authService;
        this.recentlyViewedRepository = recentlyViewedRepository;
    }

    // TODO do I need a different DTO for creating a recipe?
    public Recipe createRecipe(CreateRecipeDto createRecipeDto)
    {
        User user = authService.getLoggedInUser();

        Recipe recipe = new Recipe();

        recipe.setTitle(createRecipeDto.getTitle());
        recipe.setUserId(user.getId());
        recipe.setRating(0L);
        recipe.setServes(1L);
        recipe.setDifficulty("Medium");
        recipe.setTotalTime("00:00");

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
        getRecipe(recipeDto.getId());

        recipeRepository.deleteById(recipeDto.getId());
    }

    public void updateRecipe(RecipeDto recipeDto) throws RecipeDoesNotExistException
    {
        Recipe recipe = getRecipe(recipeDto.getId());

        recipe.setNotes(recipeDto.getNotes());
        recipe.setRating(recipeDto.getRating());
        recipe.setServes(recipeDto.getServes());
        recipe.setCookTime(recipeDto.getCookTime());
        recipe.setPrepTime(recipeDto.getPrepTime());
        recipe.setTotalTime(RecipeTime.combineCookAndPrepTime(recipeDto.getCookTime(), recipeDto.getPrepTime()));
        recipe.setDifficulty(recipeDto.getDifficulty());

        // TODO this seems a bad way of managing the one to many relationship
        ingredientRepository.deleteByRecipe(recipe);
        List<Ingredient> ingredients = new ArrayList<>();
        for (String ingredient : recipeDto.getIngredients())
        {
            ingredients.add(new Ingredient(recipe, ingredient));
        }
        recipe.setIngredients(ingredients);

        stepRepository.deleteByRecipe(recipe);
        List<Step> steps = new ArrayList<>();
        for (String step : recipeDto.getSteps())
        {
            steps.add(new Step(recipe, step));
        }
        recipe.setSteps(steps);

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
        sharedRecipe.setTotalTime(recipe.getTotalTime());
        sharedRecipe.setServes(recipe.getServes());
        sharedRecipe.setNotes(recipe.getNotes());
        sharedRecipe.setSharedBy(loggedInUser.getUsername());
        sharedRecipe.setUserId(user.getId());
        sharedRecipe.setRating(recipe.getRating());
        sharedRecipe.setIngredients(new ArrayList<>(recipe.getIngredients()));
        sharedRecipe.setSteps(new ArrayList<>(recipe.getSteps()));

        recipeRepository.save(sharedRecipe);
    }

    public void addRecentlyViewed(Recipe recipe)
    {
        Recipe mostRecentRecipe = null;
        List<RecentlyViewed> recentlyViewedRecipes = getRecentlyViewed();

        if (recentlyViewedRecipes.size() > 0)
        {
            mostRecentRecipe = recentlyViewedRecipes.get(0).getRecipe();
        }

        if (mostRecentRecipe == null || !mostRecentRecipe.getId().equals(recipe.getId()))
        {
            RecentlyViewed recentlyViewed = new RecentlyViewed();
            recentlyViewed.setRecipe(recipe);
            recentlyViewed.setUserId(recipe.getUserId());
            recentlyViewedRepository.save(recentlyViewed);
        }
    }

    public List<RecentlyViewed> getRecentlyViewed()
    {
        User user = authService.getLoggedInUser();

        return recentlyViewedRepository.findTop5ByUserIdOrderByIdDesc(user.getId());
    }

    public void deleteAllRecipes()
    {
        User user = authService.getLoggedInUser();

        recipeRepository.deleteAllByUserId(user.getId());
    }
}
