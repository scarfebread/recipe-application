package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.InventoryItemDto;
import recipeapplication.exception.InventoryItemNotFoundException;
import recipeapplication.model.InventoryItem;
import recipeapplication.model.ShoppingListItem;
import recipeapplication.model.User;
import recipeapplication.repository.InventoryRepository;
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

        return shoppingListRepository.findByUserId(user.getId());
    }

    public void deleteShoppingListItem(InventoryItemDto inventoryItemDto) throws InventoryItemNotFoundException
    {
        User user = authService.getLoggedInUser();

        Optional<InventoryItem> inventoryItem = inventoryRepository.findByIdAndUserId(inventoryItemDto.getId(), user.getId());

        if (!inventoryItem.isPresent())
        {
            throw new InventoryItemNotFoundException();
        }

        inventoryRepository.delete(inventoryItem.get());
    }

    public InventoryItem createInventoryItem(InventoryItemDto inventoryItemDto)
    {
        User user = authService.getLoggedInUser();

        InventoryItem inventoryItem = new InventoryItem();

        inventoryItem.setUserId(user.getId());
        inventoryItem.setIngredient(inventoryItemDto.getIngredient());
        inventoryItem.setQuantity(inventoryItemDto.getQuantity());

        return inventoryRepository.save(inventoryItem);
    }
}
