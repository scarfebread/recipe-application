package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.InventoryItemDto;
import recipeapplication.dto.ShoppingListItemDto;
import recipeapplication.exception.ShoppingListItemNotFoundException;
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
    private InventoryService inventoryService;
    private AuthService authService;

    @Autowired
    public ShoppingListService(
            ShoppingListRepository shoppingListRepository,
            InventoryService inventoryService,
            AuthService authService)
    {
        this.shoppingListRepository = shoppingListRepository;
        this.inventoryService = inventoryService;
        this.authService = authService;
    }

    public List<ShoppingListItem> getShoppingList()
    {
        User user = authService.getLoggedInUser();

        return shoppingListRepository.findByUserId(user.getId());
    }

    public void deleteShoppingListItem(ShoppingListItemDto shoppingListItemDto) throws ShoppingListItemNotFoundException
    {
        User user = authService.getLoggedInUser();

        Optional<ShoppingListItem> shoppingListItem = shoppingListRepository.findByIdAndUserId(shoppingListItemDto.getId(), user.getId());

        if (!shoppingListItem.isPresent())
        {
            throw new ShoppingListItemNotFoundException();
        }

        shoppingListRepository.delete(shoppingListItem.get());
    }

    public void deleteShoppingListItem(InventoryItem inventoryItem) throws ShoppingListItemNotFoundException
    {
        User user = authService.getLoggedInUser();

        Optional<ShoppingListItem> shoppingListItem = shoppingListRepository.findByInventoryIdAndUserId(inventoryItem.getId(), user.getId());

        if (!shoppingListItem.isPresent())
        {
            throw new ShoppingListItemNotFoundException();
        }

        shoppingListRepository.delete(shoppingListItem.get());
    }

    public ShoppingListItem createShoppingListItem(ShoppingListItemDto shoppingListItemDto)
    {
        User user = authService.getLoggedInUser();

        ShoppingListItem shoppingListItem = new ShoppingListItem();

        shoppingListItem.setUserId(user.getId());
        shoppingListItem.setIngredient(shoppingListItemDto.getIngredient());
        shoppingListItem.setQuantity(shoppingListItemDto.getQuantity());

        return shoppingListRepository.save(shoppingListItem);
    }

    public void purchaseIngredient(ShoppingListItemDto shoppingListItemDto) throws ShoppingListItemNotFoundException
    {
        deleteShoppingListItem(shoppingListItemDto);

        InventoryItemDto inventoryItemDto = new InventoryItemDto();

        inventoryItemDto.setIngredient(shoppingListItemDto.getIngredient());
        inventoryItemDto.setQuantity(shoppingListItemDto.getQuantity());

        inventoryService.createInventoryItem(inventoryItemDto);
    }
}
