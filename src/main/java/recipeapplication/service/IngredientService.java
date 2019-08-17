package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.model.User;
import recipeapplication.repository.IngredientRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class IngredientService
{
    private IngredientRepository ingredientRepository;
    private AuthService authService;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, AuthService authService)
    {
        this.ingredientRepository = ingredientRepository;
        this.authService = authService;
    }

    public void deleteAll()
    {
        User user = authService.getLoggedInUser();
        ingredientRepository.deleteAllByUser(user);
    }
}
