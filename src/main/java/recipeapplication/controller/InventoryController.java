package recipeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import recipeapplication.dto.InventoryItemDto;
import recipeapplication.exception.InventoryItemNotFoundException;
import recipeapplication.model.InventoryItem;
import recipeapplication.service.InventoryService;

import javax.validation.Valid;
import java.util.List;

@RestController("/api/inventory")
public class InventoryController
{
    private InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService)
    {
        this.inventoryService = inventoryService;
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
}
