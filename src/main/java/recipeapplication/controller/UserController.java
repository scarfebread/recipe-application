package recipeapplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(name = "/api/user")
public class UserController
{
    @DeleteMapping
    public ResponseEntity deleteUser()
    {
        return ResponseEntity.status(501).body("Not implemented");
    }
}
