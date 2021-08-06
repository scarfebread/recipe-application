package thecookingpot.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import thecookingpot.dto.ShoppingListItemDto;
import thecookingpot.exception.IngredientDoesNotExistException;
import thecookingpot.exception.ShoppingListItemNotFoundException;
import thecookingpot.model.Ingredient;
import thecookingpot.model.ShoppingListItem;
import thecookingpot.service.InventoryService;
import thecookingpot.service.ShoppingListService;

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

        doThrow(ShoppingListItemNotFoundException.class).when(shoppingListService).getShoppingListItem(shoppingListItemDto);

        ResponseEntity responseEntity = shoppingListController.purchaseIngredient(shoppingListItemDto);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals("Shopping list item not found", responseEntity.getBody());
    }

    @Test
    public void shouldPurchaseShoppingListItemSuccessfullyForValidShoppingListItem() throws Exception
    {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();
        Ingredient ingredient = new thecookingpot.model.Ingredient();
        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingListItem.setIngredient(ingredient);

        when(shoppingListService.getShoppingListItem(shoppingListItemDto)).thenReturn(shoppingListItem);

        ResponseEntity responseEntity = shoppingListController.purchaseIngredient(shoppingListItemDto);

        verify(shoppingListService).deleteShoppingListItem(shoppingListItemDto);
        verify(inventoryService).createInventoryItem(ingredient);

        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals("Created", responseEntity.getBody());
    }

    @Test
    public void shouldReturn404WhenRemovingShoppingListItemByIngredientIdThatDoesNotExist() throws Exception
    {
        Long ingredientId = 12345L;

        doThrow(ShoppingListItemNotFoundException.class).when(shoppingListService).removeFromShoppingList(ingredientId);

        ResponseEntity response = shoppingListController.removeFromShoppingList(ingredientId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Shopping list entry not found", response.getBody());
    }

    @Test
    public void shouldRemoveShoppingListItemByIngredientId() throws Exception
    {
        Long ingredientId = 12345L;

        ResponseEntity response = shoppingListController.removeFromShoppingList(ingredientId);

        verify(shoppingListService).removeFromShoppingList(ingredientId);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Removed successfully", response.getBody());
    }

    @Test
    public void shouldReturn404WhenAddingIngredientThatDoesNotExist() throws Exception
    {
        Long ingredientId = 12345L;

        doThrow(IngredientDoesNotExistException.class).when(shoppingListService).addToShoppingList(ingredientId);

        ResponseEntity response = shoppingListController.addToShoppingList(ingredientId);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Ingredient not found", response.getBody());
    }

    @Test
    public void shouldAddShoppingListItemByIngredientId() throws Exception
    {
        Long ingredientId = 12345L;

        ResponseEntity response = shoppingListController.addToShoppingList(ingredientId);

        verify(shoppingListService).addToShoppingList(ingredientId);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Added successfully", response.getBody());
    }
}
