package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import recipeapplication.dto.InventoryItemDto;
import recipeapplication.exception.InventoryItemNotFoundException;
import recipeapplication.exception.ShoppingListItemNotFoundException;
import recipeapplication.model.InventoryItem;
import recipeapplication.service.InventoryService;
import recipeapplication.service.ShoppingListService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/inventory")
public class InventoryController
{
    private InventoryService inventoryService;
    private ShoppingListService shoppingListService;

    @Autowired
    public InventoryController(InventoryService inventoryService, ShoppingListService shoppingListService)
    {
        this.inventoryService = inventoryService;
        this.shoppingListService = shoppingListService;
    }

    @PostMapping
    public ResponseEntity createInventoryItem(@Valid @RequestBody InventoryItemDto inventoryItemDto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.status(400).body("Invalid inventory item");
        }

        InventoryItem inventoryItem = inventoryService.createInventoryItem(inventoryItemDto);

        return ResponseEntity.status(201).body(inventoryItem);
    }

    @GetMapping
    public List<InventoryItem> getInventoryItems()
    {
        return inventoryService.getInventoryItems();
    }

    @DeleteMapping
    public ResponseEntity deleteInventoryItem(InventoryItemDto inventoryItemDto)
    {
        try
        {
            inventoryService.deleteInventoryItem(inventoryItemDto);
        }
        catch (InventoryItemNotFoundException e)
        {
            return ResponseEntity.status(404).body("Inventory item not found");
        }

        return ResponseEntity.status(202).body("Deleted successfully");
    }

    @PostMapping("/shopping-list")
    public ResponseEntity addToShoppingList(InventoryItemDto inventoryItemDto)
    {
        try
        {
            InventoryItem inventoryItem = inventoryService.getInventoryItem(inventoryItemDto);
            shoppingListService.createShoppingListItem(inventoryItem);
        }
        catch (InventoryItemNotFoundException e)
        {
            return ResponseEntity.status(404).body("Inventory item not found");
        }

        return ResponseEntity.status(201).body("Created");
    }

    @DeleteMapping("/shopping-list")
    public ResponseEntity removeFromShoppingList(InventoryItemDto inventoryItemDto)
    {
        try
        {
            InventoryItem inventoryItem = inventoryService.getInventoryItem(inventoryItemDto);
            shoppingListService.deleteShoppingListItem(inventoryItem);
        }
        catch (InventoryItemNotFoundException | ShoppingListItemNotFoundException e)
        {
            return ResponseEntity.status(404).body("Not found");
        }

        return ResponseEntity.status(202).body("Deleted successfully");
    }
}
