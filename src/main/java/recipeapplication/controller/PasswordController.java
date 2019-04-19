package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import recipeapplication.dto.PasswordDto;
import recipeapplication.dto.UserDto;
import recipeapplication.exception.UserNotFoundException;
import recipeapplication.security.RecipeUserDetails;
import recipeapplication.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class PasswordController
{
    private UserService userService;

    @Autowired
    public PasswordController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/password_reset")
    public ResponseEntity passwordReminder(HttpServletRequest request, @RequestBody UserDto userDto)
    {
        String serverName = String.format("%s:%s", request.getServerName(), request.getServerPort());

        try
        {
            userService.createPasswordResetToken(serverName, userDto);
        }
        catch (UserNotFoundException e)
        {
            // We don't show the client that the user doesn't exist for security reasons
        }

        return ResponseEntity.status(201).body("Created");
    }

    @PostMapping("/change_password")
    public ResponseEntity changePassword(@Valid @RequestBody PasswordDto passwordDto, Errors errors)
    {
        RecipeUserDetails user = (RecipeUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null)
        {
            return ResponseEntity.status(400).body("Invalid session");
        }

        if (errors.hasErrors())
        {
            return ResponseEntity.status(400).body("Invalid password");
        }

        userService.changePassword(user.getUser(), passwordDto.getPassword());

        return ResponseEntity.status(201).body("Created");
    }
}
