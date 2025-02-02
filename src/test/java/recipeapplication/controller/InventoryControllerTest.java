package recipeapplication.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import recipeapplication.dto.InventoryItemDto;
import recipeapplication.exception.InventoryItemNotFoundException;
import recipeapplication.model.InventoryItem;
import recipeapplication.service.InventoryService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class InventoryControllerTest
{
    private InventoryService inventoryService;
    private InventoryController inventoryController;
    private Errors noErrors;
    private Errors errors;

    @Before
    public void setup()
    {
        inventoryService = mock(InventoryService.class);

        errors = mock(Errors.class);
        noErrors = mock(Errors.class);

        when(errors.hasErrors()).thenReturn(true);
        when(noErrors.hasErrors()).thenReturn(false);

        inventoryController = new InventoryController(inventoryService);
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

        when(inventoryService.getInventory()).thenReturn(inventoryItems);

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
}
