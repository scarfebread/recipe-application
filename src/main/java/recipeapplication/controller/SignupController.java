package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;
import recipeapplication.exception.UsernameExistsException;
import recipeapplication.security.SignupService;
import recipeapplication.security.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ResponseEntity signup(@RequestBody UserDto userDto, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            signupService.registerNewUser(userDto);
        }
        catch (UsernameExistsException e)
        {
            return ResponseEntity.status(400).body("Username already exists");
        }

        // TODO automatic login needs fixing
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
        authToken.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManger.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.status(201).body("Created");
    }

}
