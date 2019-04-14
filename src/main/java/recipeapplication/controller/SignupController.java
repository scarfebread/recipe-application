package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import recipeapplication.exception.UsernameExistsException;
import recipeapplication.service.SignupService;
import recipeapplication.dto.UserDto;

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
    public ResponseEntity signup(@Valid @RequestBody UserDto userDto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.status(400).body("Invalid user information supplied");
        }

        try
        {
            signupService.registerNewUser(userDto);
        }
        catch (UsernameExistsException e)
        {
            return ResponseEntity.status(400).body("Username already exists");
        }

        return ResponseEntity.status(201).body("Created");
    }

}
