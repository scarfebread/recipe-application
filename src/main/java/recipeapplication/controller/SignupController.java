package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import recipeapplication.exception.UsernameExistsException;
import recipeapplication.security.SignupService;
import recipeapplication.security.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/signup")
public class SignupController
{
    private SignupService signupService;

    @Autowired
    public SignupController(SignupService signupService)
    {
        this.signupService = signupService;
    }

    @PostMapping
    public ResponseEntity signup (
            @ModelAttribute("user") @Valid UserDto userDto,
            BindingResult result,
            WebRequest request,
            Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.status(500).body("Invalid user");
        }

        try
        {
            signupService.registerNewUser(userDto);
        }
        catch (UsernameExistsException e)
        {
            return ResponseEntity.status(500).body("Username already exists");
        }

        return ResponseEntity.status(201).body("Created");
    }
}
