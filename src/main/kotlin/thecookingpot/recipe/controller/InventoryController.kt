package thecookingpot.recipe.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import thecookingpot.recipe.dto.InventoryItemDto
import thecookingpot.recipe.exception.InventoryItemNotFoundException
import thecookingpot.recipe.model.InventoryItem
import thecookingpot.recipe.service.InventoryService
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/api/inventory"])
class InventoryController @Autowired constructor(private val inventoryService: InventoryService) {
    @GetMapping
    fun getInventoryItems(): List<InventoryItem> {
        return inventoryService.inventory
    }

    @PostMapping
    fun createInventoryItem(@RequestBody inventoryItemDto: @Valid InventoryItemDto, errors: Errors): ResponseEntity<*> {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body("Invalid inventory item")
        }

        return ResponseEntity
                .status(201)
                .body(inventoryService.createInventoryItem(inventoryItemDto))
    }

    @DeleteMapping
    fun deleteInventoryItem(@RequestBody inventoryItemDto: InventoryItemDto): ResponseEntity<*> {
        try {
            inventoryService.deleteInventoryItem(inventoryItemDto)
        } catch (e: InventoryItemNotFoundException) {
            return ResponseEntity.status(404).body("Inventory item not found")
        }

        return ResponseEntity.status(202).body("Deleted successfully")
    }
}