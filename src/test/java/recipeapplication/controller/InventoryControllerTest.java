package recipeapplication.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import recipeapplication.dto.InventoryItemDto;
import recipeapplication.exception.InventoryItemNotFoundException;
import recipeapplication.model.InventoryItem;
import recipeapplication.service.InventoryService;
import recipeapplication.service.ShoppingListService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class InventoryControllerTest
{
    private InventoryService inventoryService;
    private ShoppingListService shoppingListService;
    private InventoryController inventoryController;
    private Errors noErrors;
    private Errors errors;

    @Before
    public void setup()
    {
        inventoryService = mock(InventoryService.class);
        shoppingListService = mock(ShoppingListService.class);

        errors = mock(Errors.class);
        noErrors = mock(Errors.class);

        when(errors.hasErrors()).thenReturn(true);
        when(noErrors.hasErrors()).thenReturn(false);

        inventoryController = new InventoryController(inventoryService, shoppingListService);
    }

    @Test
    public void shouldReturnInvalidInventoryItemWhenRequestHasErrors()
    {
        ResponseEntity responseEntity = inventoryController.createInventoryItem(new InventoryItemDto(), errors);

        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("Invalid inventory item", responseEntity.getBody());
    }

    @Test
    public void shouldCreateInventoryItemWhenRequestHasNoErrors()
    {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();
        InventoryItem inventoryItem = new InventoryItem();

        when(inventoryService.createInventoryItem(inventoryItemDto)).thenReturn(inventoryItem);

        ResponseEntity responseEntity = inventoryController.createInventoryItem(inventoryItemDto, noErrors);

        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals(inventoryItem, responseEntity.getBody());
    }

    @Test
    public void shouldGetInventoryItemsSuccessfully()
    {
        List<InventoryItem> inventoryItems = new ArrayList<>();

        inventoryItems.add(new InventoryItem());
        inventoryItems.add(new InventoryItem());

        when(inventoryService.getInventoryItems()).thenReturn(inventoryItems);

        assertEquals(inventoryItems, inventoryController.getInventoryItems());
    }

    @Test
    public void shouldReturnInventoryItemNotFoundWhenDoesNotExist() throws Exception
    {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();

        doThrow(InventoryItemNotFoundException.class).when(inventoryService).deleteInventoryItem(inventoryItemDto);

        ResponseEntity responseEntity = inventoryController.deleteInventoryItem(inventoryItemDto);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals("Inventory item not found", responseEntity.getBody());
    }

    @Test
    public void shouldDeleteInventoryItemSuccessfullyForValidInventoryItem() throws Exception
    {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();

        ResponseEntity responseEntity = inventoryController.deleteInventoryItem(inventoryItemDto);

        verify(inventoryService).deleteInventoryItem(inventoryItemDto);

        assertEquals(202, responseEntity.getStatusCodeValue());
        assertEquals("Deleted successfully", responseEntity.getBody());
    }

    @Test
    public void shouldReturnInventoryItemNotFoundWhenDoesNotExistWhenAddingToShoppingList() throws Exception
    {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();

        doThrow(InventoryItemNotFoundException.class).when(inventoryService).getInventoryItem(inventoryItemDto);

        ResponseEntity responseEntity = inventoryController.addToShoppingList(inventoryItemDto);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals("Inventory item not found", responseEntity.getBody());
    }

    @Test
    public void shouldReturnCreatedWhenAddedToShoppingListWithValidInventoryItem() throws Exception
    {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();
        InventoryItem inventoryItem = new InventoryItem();

        when(inventoryService.getInventoryItem(inventoryItemDto)).thenReturn(inventoryItem);

        ResponseEntity responseEntity = inventoryController.addToShoppingList(inventoryItemDto);

        verify(shoppingListService).createShoppingListItem(inventoryItem);

        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals("Created", responseEntity.getBody());
    }

    @Test
    public void shouldReturnInventoryItemNotFoundWhenDoesNotExistWhenRemovingFromShoppingList() throws Exception
    {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();

        doThrow(InventoryItemNotFoundException.class).when(inventoryService).getInventoryItem(inventoryItemDto);

        ResponseEntity responseEntity = inventoryController.removeFromShoppingList(inventoryItemDto);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals("Not found", responseEntity.getBody());
    }

    @Test
    public void shouldReturnCreatedWhenRemovingFromShoppingListWithValidInventoryItem() throws Exception
    {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();

        InventoryItem inventoryItem = new InventoryItem();

        when(inventoryService.getInventoryItem(inventoryItemDto)).thenReturn(inventoryItem);

        ResponseEntity responseEntity = inventoryController.removeFromShoppingList(inventoryItemDto);

        verify(shoppingListService).deleteShoppingListItem(inventoryItem);

        assertEquals(202, responseEntity.getStatusCodeValue());
        assertEquals("Deleted successfully", responseEntity.getBody());
    }
}
