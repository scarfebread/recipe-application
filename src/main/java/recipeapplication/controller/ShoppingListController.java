package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import recipeapplication.dto.ShoppingListItemDto;
import recipeapplication.exception.ShoppingListItemNotFoundException;
import recipeapplication.model.ShoppingListItem;
import recipeapplication.service.InventoryService;
import recipeapplication.service.ShoppingListService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/shoppingList")
public class ShoppingListController
{
    private ShoppingListService shoppingListService;
    private InventoryService inventoryService;

    @Autowired
    public ShoppingListController(ShoppingListService shoppingListService, InventoryService inventoryService)
    {
        this.shoppingListService = shoppingListService;
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity createShoppingListItem(@Valid @RequestBody ShoppingListItemDto shoppingListItemDto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.status(400).body("Invalid shopping list item");
        }

        ShoppingListItem shoppingListItem = shoppingListService.createShoppingListItem(shoppingListItemDto);

        return ResponseEntity.status(201).body(shoppingListItem);
    }

    @GetMapping
    public List<ShoppingListItem> getShoppingListItems()
    {
        return shoppingListService.getShoppingList();
    }

    @DeleteMapping
    public ResponseEntity deleteShoppingListItem(@RequestBody ShoppingListItemDto shoppingListItemDto)
    {
        try
        {
            shoppingListService.deleteShoppingListItem(shoppingListItemDto);
        }
        catch (ShoppingListItemNotFoundException e)
        {
            return ResponseEntity.status(404).body("Shopping list item not found");
        }

        return ResponseEntity.status(202).body("Deleted successfully");
    }

    @PostMapping("/purchase")
    public ResponseEntity purchaseIngredient(@RequestBody ShoppingListItemDto shoppingListItemDto)
    {
        try
        {
            ShoppingListItem shoppingListItem = shoppingListService.getShoppingListItem(shoppingListItemDto);
            inventoryService.createInventoryItem(shoppingListItem.getIngredient());
            shoppingListService.deleteShoppingListItem(shoppingListItemDto);
        }
        catch (ShoppingListItemNotFoundException e)
        {
            return ResponseEntity.status(404).body("Shopping list item not found");
        }

        return ResponseEntity.status(201).body("Created");
    }
}
