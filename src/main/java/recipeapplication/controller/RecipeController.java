package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import recipeapplication.dto.*;
import recipeapplication.exception.IngredientDoesNotExistException;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.exception.SameUsernameException;
import recipeapplication.exception.UserNotFoundException;
import recipeapplication.model.Ingredient;
import recipeapplication.model.Recipe;
import recipeapplication.model.Step;
import recipeapplication.model.User;
import recipeapplication.service.InventoryService;
import recipeapplication.service.RecipeService;
import recipeapplication.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/recipe")
public class RecipeController
{
    private RecipeService recipeService;
    private InventoryService inventoryService;
    private UserService userService;

    @Autowired
    public RecipeController(RecipeService recipeService, InventoryService inventoryService, UserService userService)
    {
        this.recipeService = recipeService;
        this.inventoryService = inventoryService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity createRecipe(@Valid @RequestBody CreateRecipeDto recipeDto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.status(400).body("Invalid recipe");
        }

        Recipe recipe = recipeService.createRecipe(recipeDto);

        return ResponseEntity.status(201).body(recipe);
    }

    @GetMapping
    public List<Recipe> getRecipes()
    {
        return recipeService.getRecipes();
    }

    @DeleteMapping
    public ResponseEntity deleteRecipe(@RequestBody RecipeDto recipeDto)
    {
        if (recipeDto.getId() == null)
        {
            return ResponseEntity.status(400).body("No recipe supplied");
        }

        try
        {
            recipeService.deleteRecipe(recipeDto);
        }
        catch (RecipeDoesNotExistException e)
        {
            return ResponseEntity.status(404).body("Recipe does not exist");
        }

        return ResponseEntity.status(202).body("Deleted successfully");
    }

    @PutMapping
    public ResponseEntity updateRecipe(@RequestBody RecipeDto recipeDto)
    {
        if (recipeDto.getId() == null)
        {
            return ResponseEntity.status(400).body("No recipe supplied");
        }

        try
        {
            recipeService.updateRecipe(recipeDto);
            userService.turnOffInstructions();
        }
        catch (RecipeDoesNotExistException e)
        {
            return ResponseEntity.status(404).body("Recipe does not exist");
        }

        return ResponseEntity.status(202).body("Updated");
    }

    @PutMapping(path = "/ingredient")
    public ResponseEntity addIngredient(@Valid @RequestBody IngredientDto ingredientDto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.status(400).body("Invalid ingredient");
        }

        Ingredient ingredient;

        try
        {
            userService.turnOffInstructions();
            ingredient = recipeService.addIngredient(ingredientDto);
            ingredient.setInventoryItems(
                    inventoryService.getSimilarIngredients(ingredient)
            );
        }
        catch (RecipeDoesNotExistException e)
        {
            return ResponseEntity.status(404).body("Recipe does not exist");
        }

        return ResponseEntity.status(202).body(ingredient);
    }

    @PutMapping(path = "/delete-ingredient")
    public ResponseEntity deleteIngredient(@Valid @RequestBody DeleteIngredientDto ingredientDto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.status(400).body("Invalid ingredient supplied");
        }

        try
        {
            recipeService.deleteIngredient(ingredientDto);
        }
        catch (IngredientDoesNotExistException | RecipeDoesNotExistException e)
        {
            return ResponseEntity.status(404).body("Ingredient not found");
        }

        return ResponseEntity.status(202).body("Ingredient deleted");
    }

    @PutMapping(path = "/add-step")
    public ResponseEntity addStep(@Valid @RequestBody CreateStepDto stepDto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.status(400).body("Invalid step");
        }

        Step step;

        try
        {
            userService.turnOffInstructions();
            step = recipeService.addStep(stepDto);
        }
        catch (RecipeDoesNotExistException e)
        {
            return ResponseEntity.status(404).body("Recipe does not exist");
        }

        return ResponseEntity.status(202).body(step);
    }

    @PutMapping(path = "/delete-step")
    public ResponseEntity deleteStep(@Valid @RequestBody DeleteStepDto stepDto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.status(400).body("Invalid step supplied");
        }

        try
        {
            recipeService.deleteStep(stepDto);
        }
        catch (RecipeDoesNotExistException e)
        {
            return ResponseEntity.status(404).body("Recipe not found");
        }

        return ResponseEntity.status(202).body("Step deleted");
    }

    @PutMapping(path = "/update-step")
    public ResponseEntity updateStep(@Valid @RequestBody UpdateStepDto stepDto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.status(400).body("Invalid step supplied");
        }

        try
        {
            recipeService.updateStep(stepDto);
        }
        catch (RecipeDoesNotExistException e)
        {
            return ResponseEntity.status(404).body("Recipe not found");
        }

        return ResponseEntity.status(202).body("Step updated");
    }

    @PostMapping(path = "/share")
    public ResponseEntity shareRecipe(@RequestBody RecipeDto recipeDto)
    {
        if (recipeDto.getId() == null)
        {
            return ResponseEntity.status(400).body("No recipe supplied");
        }

        if (recipeDto.getNewUser() == null)
        {
            return ResponseEntity.status(400).body("No user supplied");
        }

        try
        {
            User user = userService.getUser(recipeDto.getNewUser());

            recipeService.shareRecipe(recipeDto, user);
        }
        catch (RecipeDoesNotExistException e)
        {
            return ResponseEntity.status(404).body("Recipe does not exist");
        }
        catch (UserNotFoundException e)
        {
            return ResponseEntity.status(404).body("User does not exist");
        }
        catch (SameUsernameException e)
        {
            return ResponseEntity.status(400).body("You already have this recipe");
        }

        return ResponseEntity.status(201).body("Created");
    }
}
