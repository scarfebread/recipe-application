package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import recipeapplication.dto.CreateRecipeDto;
import recipeapplication.dto.RecipeDto;
import recipeapplication.exception.RecipeDoesNotExistException;
import recipeapplication.model.Recipe;
import recipeapplication.service.RecipeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/recipe")
public class RecipeController
{
    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService)
    {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity createRecipe(@Valid @RequestBody CreateRecipeDto recipeDto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.status(400).body("Invalid recipe");
        }

        recipeService.createRecipe(recipeDto);

        return ResponseEntity.status(201).body("Created");
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
            // TODO is 400 the right status
            return ResponseEntity.status(400).body("No recipe supplied");
        }

        try
        {
            recipeService.deleteRecipe(recipeDto);
        }
        catch (RecipeDoesNotExistException e)
        {
            return ResponseEntity.status(400).body("Recipe does not exist");
        }

        return ResponseEntity.status(200).body("Deleted successfully");
    }
}
