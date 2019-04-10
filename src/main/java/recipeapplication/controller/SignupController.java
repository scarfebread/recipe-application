package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
    private DaoAuthenticationProvider authenticationManger;

    @Autowired
    public SignupController(SignupService signupService, DaoAuthenticationProvider authenticationManger)
    {
        this.signupService = signupService;
        this.authenticationManger = authenticationManger;
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

        // TODO automatic login needs fixing
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
//        authToken.setDetails(new WebAuthenticationDetails(request));
//
//        Authentication authentication = authenticationManger.authenticate(authToken);
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.status(201).body("Created");
    }

}
