package recipeapplication.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.CreateRecipeDto;
import recipeapplication.dto.IngredientDto;
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

import javax.persistence.EntityManager;
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
    private EntityManager entityManager;

    @Autowired
    public RecipeService(
            RecipeRepository recipeRepository,
            IngredientRepository ingredientRepository,
            StepRepository stepRepository,
            AuthService authService,
            RecentlyViewedRepository recentlyViewedRepository,
            EntityManager entityManager)
    {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.stepRepository = stepRepository;
        this.authService = authService;
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.entityManager = entityManager;
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
        User user = authService.getLoggedInUser();

        recipe.setNotes(recipeDto.getNotes());
        recipe.setRating(recipeDto.getRating());
        recipe.setServes(recipeDto.getServes());
        recipe.setCookTime(recipeDto.getCookTime());
        recipe.setPrepTime(recipeDto.getPrepTime());
        recipe.setTotalTime(RecipeTime.combineCookAndPrepTime(recipeDto.getCookTime(), recipeDto.getPrepTime()));
        recipe.setDifficulty(recipeDto.getDifficulty());

        List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientDto ingredient : recipeDto.getIngredients())
        {
            ingredients.add(new Ingredient(ingredient.getDescription(), ingredient.getQuantity(), user));
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

        // Clone the existing recipe
        Hibernate.initialize(recipe.getIngredients());
        Hibernate.initialize(recipe.getSteps());
        entityManager.detach(recipe);
        recipe.setId(null);

        recipe.setSharedBy(authService.getLoggedInUser().getUsername());
        recipe.setUserId(user.getId());

        for (Ingredient ingredient : recipe.getIngredients())
        {
            entityManager.detach(ingredient);
            ingredient.setId(null);
        }

        for (Step step : recipe.getSteps())
        {
            entityManager.detach(step);
            step.setId(null);
        }

        recipeRepository.save(recipe);
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

    public Ingredient addIngredient(IngredientDto ingredientDto) throws RecipeDoesNotExistException
    {
        Recipe recipe = getRecipe(ingredientDto.getRecipe());
        User user = authService.getLoggedInUser();

        Ingredient ingredient = new Ingredient(ingredientDto.getDescription(), ingredientDto.getQuantity(), user);

        recipe.addIngredient(ingredient);

        recipeRepository.save(recipe);

        return ingredient;
    }
}
