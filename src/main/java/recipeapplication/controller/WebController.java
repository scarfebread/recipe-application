package recipeapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController
{
    @GetMapping("/login")
    public String getLogin()
    {
        return "login.html";
    }

    @GetMapping("/")
    public String getIndex()
    {
        return "home.html";
    }

    @GetMapping("/signup")
    public String getSignup()
    {
        return "signup.html";
    }

    @GetMapping("/recipe")
    public String getRecipe()
    {
        return "recipe.html";
    }
}
