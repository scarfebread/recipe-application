package recipeapplication.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.*;
import recipeapplication.exception.IngredientDoesNotExistException;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.exception.SameUsernameException;
import recipeapplication.model.*;
import recipeapplication.repository.IngredientRepository;
import recipeapplication.repository.RecentlyViewedRepository;
import recipeapplication.repository.RecipeRepository;
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
    private AuthService authService;
    private RecentlyViewedRepository recentlyViewedRepository;
    private EntityManager entityManager;

    @Autowired
    public RecipeService(
            RecipeRepository recipeRepository,
            IngredientRepository ingredientRepository,
            AuthService authService,
            RecentlyViewedRepository recentlyViewedRepository,
            EntityManager entityManager)
    {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.authService = authService;
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.entityManager = entityManager;
    }

    public Recipe createRecipe(CreateRecipeDto createRecipeDto)
    {
        User user = authService.getLoggedInUser();

        Recipe recipe = new Recipe();

        recipe.setTitle(createRecipeDto.getTitle());
        recipe.setUser(user);
        recipe.setRating(0L);
        recipe.setServes(1L);
        recipe.setDifficulty("Medium");
        recipe.setTotalTime("00:00");
        recipe.setCookTime("00:00");
        recipe.setPrepTime("00:00");

        return recipeRepository.save(recipe);
    }

    public List<Recipe> getRecipes()
    {
        User user = authService.getLoggedInUser();

        return recipeRepository.findByUser(user);
    }

    public Recipe getRecipe(Long id) throws RecipeDoesNotExistException
    {
        Optional<Recipe> result = recipeRepository.findByIdAndUser(id, authService.getLoggedInUser());

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

        recipeRepository.save(recipe);
    }

    public void shareRecipe(RecipeDto recipeDto, User user) throws RecipeDoesNotExistException, SameUsernameException
    {
        User loggedInUser = authService.getLoggedInUser();
        if (loggedInUser.getUsername().equals(user.getUsername()))
        {
            throw new SameUsernameException();
        }

        Recipe recipe = getRecipe(recipeDto.getId());

        // Clone the existing recipe
        Hibernate.initialize(recipe.getIngredients());
        Hibernate.initialize(recipe.getSteps());
        entityManager.detach(recipe);
        recipe.setId(null);

        recipe.setSharedBy(authService.getLoggedInUser().getUsername());
        recipe.setUser(user);

        List<Ingredient> ingredients = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients())
        {
            ingredients.add(
                    new Ingredient(ingredient.getDescription(), ingredient.getMetric(), user)
            );
        }

        List<Step> steps = new ArrayList<>();
        for (Step step : recipe.getSteps())
        {
            steps.add(
                    new Step(step.getDescription())
            );
        }

        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);

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
            recentlyViewed.setUser(recipe.getUser());
            recentlyViewedRepository.save(recentlyViewed);
        }
    }

    public List<RecentlyViewed> getRecentlyViewed()
    {
        User user = authService.getLoggedInUser();

        return recentlyViewedRepository.findTop5ByUserOrderByIdDesc(user);
    }

    public void deleteAllRecipes()
    {
        User user = authService.getLoggedInUser();

        recipeRepository.deleteAllByUser(user);
    }

    public Ingredient addIngredient(IngredientDto ingredientDto) throws RecipeDoesNotExistException
    {
        Recipe recipe = getRecipe(ingredientDto.getRecipe());
        User user = authService.getLoggedInUser();

        Ingredient ingredient = new Ingredient(ingredientDto.getDescription(), ingredientDto.getQuantity(), user);

        recipe.addIngredient(ingredient);

        recipeRepository.save(recipe);

        // Simply returning ingredient does not set the ID required by the front end
        return recipe.getIngredients().get(
                recipe.getIngredients().size() - 1
        );
    }

    public Step addStep(CreateStepDto stepDto) throws RecipeDoesNotExistException
    {
        Recipe recipe = getRecipe(stepDto.getRecipe());

        Step step = new Step(stepDto.getDescription());

        recipe.addStep(step);

        recipeRepository.save(recipe);

        // Simply returning the step does not set the ID required by the front end
        return recipe.getSteps().get(
                recipe.getSteps().size() - 1
        );
    }

    public void deleteIngredient(DeleteIngredientDto ingredientDto) throws RecipeDoesNotExistException, IngredientDoesNotExistException
    {
        User user = authService.getLoggedInUser();
        Recipe recipe = getRecipe(ingredientDto.getRecipeId());
        Optional<Ingredient> ingredient = ingredientRepository.findByIdAndUser(ingredientDto.getIngredientId(), user);

        if (!ingredient.isPresent())
        {
            throw new IngredientDoesNotExistException();
        }

        for (Ingredient recipeIngredient : recipe.getIngredients())
        {
            if (recipeIngredient == ingredient.get())
            {
                recipe.getIngredients().remove(recipeIngredient);
                recipeRepository.save(recipe);
                break;
            }
        }
    }

    public void deleteStep(DeleteStepDto stepDto) throws RecipeDoesNotExistException
    {
        Recipe recipe = getRecipe(stepDto.getRecipeId());
        Step step = extractStepFromRecipe(stepDto.getStepId(), recipe);

        if (step != null)
        {
            recipe.getSteps().remove(step);
            recipeRepository.save(recipe);
        }
    }

    public void updateStep(UpdateStepDto stepDto) throws RecipeDoesNotExistException
    {
        Recipe recipe = getRecipe(stepDto.getRecipe());
        Step step = extractStepFromRecipe(stepDto.getId(), recipe);

        if (step != null)
        {
            step.setDescription(stepDto.getDescription());
            recipeRepository.save(recipe);
        }
    }

    private Step extractStepFromRecipe(Long stepId, Recipe recipe)
    {
        for (Step step : recipe.getSteps())
        {
            if (step.getId().equals(stepId))
            {
                return step;
            }
        }

        return null;
    }
}
