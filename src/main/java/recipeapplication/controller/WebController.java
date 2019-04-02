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
    public String index()
    {
        return "home.html";
    }

    @GetMapping("/signup")
    public String signup()
    {
        return "signup.html";
    }

    @GetMapping("/recipe")
    public String recipe()
    {
        return "recipe.html";
    }

    @GetMapping("/resetPassword")
    public String resetPassword()
    {
        return "reset_password.html";
    }
}
