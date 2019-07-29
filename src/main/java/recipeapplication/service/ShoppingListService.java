package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.ShoppingListItemDto;
import recipeapplication.exception.ShoppingListItemNotFoundException;
import recipeapplication.model.Ingredient;
import recipeapplication.model.InventoryItem;
import recipeapplication.model.ShoppingListItem;
import recipeapplication.model.User;
import recipeapplication.repository.ShoppingListRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingListService
{
    private ShoppingListRepository shoppingListRepository;
    private AuthService authService;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository, AuthService authService)
    {
        this.shoppingListRepository = shoppingListRepository;
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

    public void deleteShoppingListItem(InventoryItem inventoryItem) throws ShoppingListItemNotFoundException
    {
        User user = authService.getLoggedInUser();

        Optional<ShoppingListItem> shoppingListItem = shoppingListRepository.findByIngredientAndUser(inventoryItem.getIngredient(), user);

        if (!shoppingListItem.isPresent())
        {
            throw new ShoppingListItemNotFoundException();
        }

        shoppingListRepository.delete(shoppingListItem.get());
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

    public void createShoppingListItem(InventoryItem inventoryItem)
    {
        User user = authService.getLoggedInUser();

        ShoppingListItem shoppingListItem = new ShoppingListItem();

        shoppingListItem.setUser(user);
        shoppingListItem.setIngredient(inventoryItem.getIngredient());

        shoppingListRepository.save(shoppingListItem);
    }
}
