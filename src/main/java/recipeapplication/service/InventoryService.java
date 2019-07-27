package recipeapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipeapplication.dto.InventoryItemDto;
import recipeapplication.dto.ShoppingListItemDto;
import recipeapplication.exception.InventoryItemNotFoundException;
import recipeapplication.model.Ingredient;
import recipeapplication.model.InventoryItem;
import recipeapplication.model.User;
import recipeapplication.repository.IngredientRepository;
import recipeapplication.repository.InventoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService
{
    private InventoryRepository inventoryRepository;
    private IngredientRepository ingredientRepository;
    private AuthService authService;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, IngredientRepository ingredientRepository, AuthService authService)
    {
        this.inventoryRepository = inventoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.authService = authService;
    }

    public List<InventoryItem> getInventory()
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

        Ingredient ingredient = new Ingredient(inventoryItemDto.getIngredient(), inventoryItemDto.getQuantity(), user);
        InventoryItem inventoryItem = new InventoryItem();

        inventoryItem.setUser(user);
        inventoryItem.setIngredient(ingredient);

        return inventoryRepository.save(inventoryItem);
    }

    public void createInventoryItem(Ingredient ingredient)
    {
        User user = authService.getLoggedInUser();

        InventoryItem inventoryItem = new InventoryItem();

        inventoryItem.setUser(user);
        inventoryItem.setIngredient(ingredient);

        inventoryRepository.save(inventoryItem);
    }

    public InventoryItem getInventoryItem(InventoryItemDto inventoryItemDto) throws InventoryItemNotFoundException
    {
        User user = authService.getLoggedInUser();

        Optional<InventoryItem> inventoryItem = inventoryRepository.findByIdAndUserId(inventoryItemDto.getId(), user.getId());

        if (!inventoryItem.isPresent())
        {
            throw new InventoryItemNotFoundException();
        }

        return inventoryItem.get();
    }

    public List<String> getIngredients()
    {
        return ingredientRepository.getIngredients(authService.getLoggedInUser());
    }
}
