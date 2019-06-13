package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipeapplication.service.UserService;

@RestController
@RequestMapping(name = "/api/user")
public class UserController
{
    private UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @DeleteMapping
    public ResponseEntity deleteUser()
    {
        userService.deleteAccount();

        return ResponseEntity.status(202).body("Account deleted");
    }
}
