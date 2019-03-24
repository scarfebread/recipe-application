package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import recipeapplication.repository.RecipeRepository;

@RestController
@RequestMapping(path = "/recipe")
public class RecipeController
{
    private RecipeRepository recipeRepository;

    @Autowired
    public RecipeController(RecipeRepository recipeRepository)
    {
        this.recipeRepository = recipeRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getRecipes()
    {
        return recipeRepository.findAll().toString();
    }
}
