package recipeapplication.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import recipeapplication.dto.ShoppingListItemDto;
import recipeapplication.exception.ShoppingListItemNotFoundException;
import recipeapplication.model.ShoppingListItem;
import recipeapplication.service.InventoryService;
import recipeapplication.service.ShoppingListService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class ShoppingListControllerTest
{
    private ShoppingListService shoppingListService;
    private InventoryService inventoryService;
    private ShoppingListController shoppingListController;
    private Errors noErrors;
    private Errors errors;

    @Before
    public void setup()
    {
        shoppingListService = mock(ShoppingListService.class);
        inventoryService = mock(InventoryService.class);
        errors = mock(Errors.class);
        noErrors = mock(Errors.class);

        when(errors.hasErrors()).thenReturn(true);
        when(noErrors.hasErrors()).thenReturn(false);

        shoppingListController = new ShoppingListController(shoppingListService, inventoryService);
    }

    @Test
    public void shouldReturnInvalidShoppingListItemWhenRequestHasErrors()
    {
        ResponseEntity responseEntity = shoppingListController.createShoppingListItem(new ShoppingListItemDto(), errors);

        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("Invalid shopping list item", responseEntity.getBody());
    }

    @Test
    public void shouldCreateShoppingListItemWhenRequestHasNoErrors()
    {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();
        ShoppingListItem shoppingListItem = new ShoppingListItem();

        when(shoppingListService.createShoppingListItem(shoppingListItemDto)).thenReturn(shoppingListItem);

        ResponseEntity responseEntity = shoppingListController.createShoppingListItem(shoppingListItemDto, noErrors);

        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals(shoppingListItem, responseEntity.getBody());
    }

    @Test
    public void shouldGetShoppingListItemsSuccessfully()
    {
        List<ShoppingListItem> shoppingListItems = new ArrayList<>();

        shoppingListItems.add(new ShoppingListItem());
        shoppingListItems.add(new ShoppingListItem());

        when(shoppingListService.getShoppingList()).thenReturn(shoppingListItems);

        assertEquals(shoppingListItems, shoppingListController.getShoppingListItems());
    }

    @Test
    public void shouldReturnShoppingListItemNotFoundWhenDoesNotExist() throws Exception
    {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();

        doThrow(ShoppingListItemNotFoundException.class).when(shoppingListService).deleteShoppingListItem(shoppingListItemDto);

        ResponseEntity responseEntity = shoppingListController.deleteShoppingListItem(shoppingListItemDto);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals("Shopping list item not found", responseEntity.getBody());
    }

    @Test
    public void shouldDeleteShoppingListItemSuccessfullyForValidShoppingListItem() throws Exception
    {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();

        ResponseEntity responseEntity = shoppingListController.deleteShoppingListItem(shoppingListItemDto);

        verify(shoppingListService).deleteShoppingListItem(shoppingListItemDto);

        assertEquals(202, responseEntity.getStatusCodeValue());
        assertEquals("Deleted successfully", responseEntity.getBody());
    }

    @Test
    public void shouldReturnShoppingListItemNotFoundWhenDoesNotExistOnPurchase() throws Exception
    {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();

        doThrow(ShoppingListItemNotFoundException.class).when(shoppingListService).deleteShoppingListItem(shoppingListItemDto);

        ResponseEntity responseEntity = shoppingListController.purchaseIngredient(shoppingListItemDto);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals("Shopping list item not found", responseEntity.getBody());
    }

    @Test
    public void shouldPurchaseShoppingListItemSuccessfullyForValidShoppingListItem() throws Exception
    {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();

        ResponseEntity responseEntity = shoppingListController.purchaseIngredient(shoppingListItemDto);

        verify(shoppingListService).deleteShoppingListItem(shoppingListItemDto);
        verify(inventoryService).createInventoryItem(shoppingListItemDto);

        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals("Created", responseEntity.getBody());
    }
}
