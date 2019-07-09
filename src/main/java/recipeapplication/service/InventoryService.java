package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.InventoryItemDto;
import recipeapplication.dto.ShoppingListItemDto;
import recipeapplication.exception.InventoryItemNotFoundException;
import recipeapplication.exception.ShoppingListItemNotFoundException;
import recipeapplication.model.InventoryItem;
import recipeapplication.model.ShoppingListItem;
import recipeapplication.model.User;
import recipeapplication.repository.InventoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService
{
    private InventoryRepository inventoryRepository;
    private ShoppingListService shoppingListService;
    private AuthService authService;

    @Autowired
    public InventoryService(
            InventoryRepository inventoryRepository,
            ShoppingListService shoppingListService,
            AuthService authService)
    {
        this.inventoryRepository = inventoryRepository;
        this.shoppingListService = shoppingListService;
        this.authService = authService;
    }

    public List<InventoryItem> getInventoryItems()
    {
        User user = authService.getLoggedInUser();

        return inventoryRepository.findByUserId(user.getId());
    }

    public void deleteInventoryItem(InventoryItemDto inventoryItemDto) throws InventoryItemNotFoundException
    {
        inventoryRepository.delete(
                getInventoryItem(inventoryItemDto)
        );
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

    public void addToShoppingList(InventoryItemDto inventoryItemDto) throws InventoryItemNotFoundException
    {
        InventoryItem inventoryItem = getInventoryItem(inventoryItemDto);

        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();

        shoppingListItemDto.setIngredient(inventoryItem.getIngredient());
        shoppingListItemDto.setQuantity(inventoryItem.getQuantity());
        shoppingListItemDto.setInventoryItemId(inventoryItem.getId());

        shoppingListService.createShoppingListItem(shoppingListItemDto);
    }

    public void removeFromShoppingList(InventoryItemDto inventoryItemDto) throws InventoryItemNotFoundException, ShoppingListItemNotFoundException
    {
        InventoryItem inventoryItem = getInventoryItem(inventoryItemDto);

        shoppingListService.deleteShoppingListItem(inventoryItem);
    }

    private InventoryItem getInventoryItem(InventoryItemDto inventoryItemDto) throws InventoryItemNotFoundException
    {
        User user = authService.getLoggedInUser();

        Optional<InventoryItem> inventoryItem = inventoryRepository.findByIdAndUserId(inventoryItemDto.getId(), user.getId());

        if (!inventoryItem.isPresent())
        {
            throw new InventoryItemNotFoundException();
        }

        return inventoryItem.get();
    }
}
