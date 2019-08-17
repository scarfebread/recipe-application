package recipeapplication.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.CreateRecipeDto;
import recipeapplication.dto.DeleteIngredientDto;
import recipeapplication.dto.IngredientDto;
import recipeapplication.dto.RecipeDto;
import recipeapplication.exception.IngredientDoesNotExistException;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.exception.SameUsernameException;
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

        stepRepository.deleteByRecipe(recipe);
        List<Step> steps = new ArrayList<>();
        for (String step : recipeDto.getSteps())
        {
            steps.add(new Step(recipe, step));
        }
        recipe.setSteps(steps);

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
}
