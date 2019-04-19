package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import recipeapplication.dto.UserDto;
import recipeapplication.exception.InvalidPasswordTokenException;
import recipeapplication.service.UserService;

@Controller
public class WebController
{
    private UserService userService;

    @Autowired
    public WebController(UserService userService)
    {
        this.userService = userService;
    }

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
    public String signup(WebRequest request, Model model)
    {
        model.addAttribute("user", new UserDto());

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

    @GetMapping("/changePassword")
    public String changePassword(@RequestParam String token)
    {
        try
        {
            userService.processPasswordResetToken(token);

            return "change_password.html";
        }
        catch (InvalidPasswordTokenException e)
        {
            return "invalid_token.html";
        }
    }
}
