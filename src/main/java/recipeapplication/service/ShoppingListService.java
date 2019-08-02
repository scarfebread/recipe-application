package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.ShoppingListItemDto;
import recipeapplication.exception.IngredientDoesNotExistException;
import recipeapplication.exception.ShoppingListItemNotFoundException;
import recipeapplication.model.Ingredient;
import recipeapplication.model.ShoppingListItem;
import recipeapplication.model.User;
import recipeapplication.repository.IngredientRepository;
import recipeapplication.repository.ShoppingListRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingListService
{
    private ShoppingListRepository shoppingListRepository;
    private IngredientRepository ingredientRepository;
    private AuthService authService;

    @Autowired
    public ShoppingListService(
            ShoppingListRepository shoppingListRepository,
            IngredientRepository ingredientRepository,
            AuthService authService)
    {
        this.shoppingListRepository = shoppingListRepository;
        this.ingredientRepository = ingredientRepository;
        this.authService = authService;
    }

    public List<ShoppingListItem> getShoppingList()
    {
        User user = authService.getLoggedInUser();

        return shoppingListRepository.findByUser(user);
    }

    public void deleteShoppingListItem(ShoppingListItemDto shoppingListItemDto) throws ShoppingListItemNotFoundException
    {
        ShoppingListItem shoppingListItem = getShoppingListItem(shoppingListItemDto);

        shoppingListRepository.delete(shoppingListItem);
    }

    public ShoppingListItem getShoppingListItem(ShoppingListItemDto shoppingListItemDto) throws ShoppingListItemNotFoundException
    {
        User user = authService.getLoggedInUser();

        Optional<ShoppingListItem> shoppingListItem = shoppingListRepository.findByIdAndUser(shoppingListItemDto.getId(), user);

        if (!shoppingListItem.isPresent())
        {
            throw new ShoppingListItemNotFoundException();
        }

        return shoppingListItem.get();
    }

    public ShoppingListItem createShoppingListItem(ShoppingListItemDto shoppingListItemDto)
    {
        User user = authService.getLoggedInUser();

        Ingredient ingredient = new Ingredient(shoppingListItemDto.getIngredient(), shoppingListItemDto.getQuantity(), user);
        ShoppingListItem shoppingListItem = new ShoppingListItem();

        shoppingListItem.setUser(user);
        shoppingListItem.setIngredient(ingredient);

        return shoppingListRepository.save(shoppingListItem);
    }

    public void addToShoppingList(Long ingredientId) throws IngredientDoesNotExistException
    {
        User user = authService.getLoggedInUser();

        Optional<Ingredient> ingredient = ingredientRepository.findByIdAndUser(ingredientId, user);

        if (!ingredient.isPresent())
        {
            throw new IngredientDoesNotExistException();
        }

        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingListItem.setUser(user);
        shoppingListItem.setIngredient(ingredient.get());

        shoppingListRepository.save(shoppingListItem);
    }

    public void removeFromShoppingList(Long ingredientId) throws ShoppingListItemNotFoundException
    {
        User user = authService.getLoggedInUser();

        Optional<ShoppingListItem> shoppingListItem = shoppingListRepository.findByIngredientIdAndUser(ingredientId, user);

        if (shoppingListItem.isPresent())
        {
            shoppingListRepository.delete(shoppingListItem.get());
        }
        else
        {
            throw new ShoppingListItemNotFoundException();
        }
    }
}
