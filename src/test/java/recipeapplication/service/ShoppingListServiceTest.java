package recipeapplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import recipeapplication.dto.InventoryItemDto;
import recipeapplication.dto.ShoppingListItemDto;
import recipeapplication.exception.ShoppingListItemNotFoundException;
import recipeapplication.model.InventoryItem;
import recipeapplication.model.ShoppingListItem;
import recipeapplication.model.User;
import recipeapplication.repository.ShoppingListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class ShoppingListServiceTest
{
    private ShoppingListRepository shoppingListRepository;
    private ShoppingListService shoppingListService;
    private InventoryService inventoryService;
    private User user;

    @Before
    public void setup()
    {
        AuthService authService = mock(AuthService.class);

        user = new User();
        user.setId(12345L);

        when(authService.getLoggedInUser()).thenReturn(user);

        shoppingListRepository = mock(ShoppingListRepository.class);
        inventoryService = mock(InventoryService.class);

        shoppingListService = new ShoppingListService(shoppingListRepository, inventoryService, authService);
    }

    @Test
    public void shouldGetShoppingListItemsForUser()
    {
        List<ShoppingListItem> shoppingListItems = new ArrayList<>();

        shoppingListItems.add(new ShoppingListItem());
        shoppingListItems.add(new ShoppingListItem());

        when(shoppingListRepository.findByUserId(user.getId())).thenReturn(shoppingListItems);

        assertEquals(shoppingListItems, shoppingListService.getShoppingList());
    }

    @Test(expected = ShoppingListItemNotFoundException.class)
    public void shouldThrowShoppingListItemNotFoundExceptionWhenShoppingListItemDoesNotExist() throws Exception
    {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();
        shoppingListItemDto.setId(54321L);

        when(shoppingListRepository.findByIdAndUserId(shoppingListItemDto.getId(), user.getId())).thenReturn(Optional.empty());

        shoppingListService.deleteShoppingListItem(shoppingListItemDto);
    }

    @Test
    public void shouldDeleteShoppingListItemWhenShoppingListItemExistsForUser() throws Exception
    {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();
        shoppingListItemDto.setId(124542L);

        ShoppingListItem shoppingListItem = new ShoppingListItem();

        when(shoppingListRepository.findByIdAndUserId(shoppingListItemDto.getId(), user.getId())).thenReturn(Optional.of(shoppingListItem));

        shoppingListService.deleteShoppingListItem(shoppingListItemDto);

        verify(shoppingListRepository).delete(shoppingListItem);
    }

    @Test(expected = ShoppingListItemNotFoundException.class)
    public void shouldThrowShoppingListItemNotFoundExceptionWhenDeletingUsingInventoryItem() throws Exception
    {
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setId(124452L);

        when(shoppingListRepository.findByInventoryIdAndUserId(inventoryItem.getId(), user.getId())).thenReturn(Optional.empty());

        shoppingListService.deleteShoppingListItem(inventoryItem);
    }

    @Test
    public void shouldDeleteShoppingListItemWhenUsingExistingInventoryItem() throws Exception
    {
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setId(124452L);

        ShoppingListItem shoppingListItem = new ShoppingListItem();

        when(shoppingListRepository.findByInventoryIdAndUserId(inventoryItem.getId(), user.getId())).thenReturn(Optional.of(shoppingListItem));

        shoppingListService.deleteShoppingListItem(inventoryItem);

        verify(shoppingListRepository).delete(shoppingListItem);
    }

    @Test
    public void shouldCreateRecipe()
    {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();
        shoppingListItemDto.setIngredient("INGREDIENT");
        shoppingListItemDto.setQuantity("QUANTITY");

        when(shoppingListRepository.save(any(ShoppingListItem.class))).then(returnsFirstArg());

        ShoppingListItem shoppingListItem = shoppingListService.createShoppingListItem(shoppingListItemDto);

        assertEquals(shoppingListItemDto.getIngredient(), shoppingListItem.getIngredient());
        assertEquals(shoppingListItemDto.getQuantity(), shoppingListItem.getQuantity());
    }

    @Test
    public void shouldPurchaseIngredientByDeletingShoppingListAndCreatingInventoryItem() throws Exception
    {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();
        shoppingListItemDto.setIngredient("INGREDIENT");
        shoppingListItemDto.setQuantity("QUANTITY");

        ShoppingListItem shoppingListItem = new ShoppingListItem();

        when(shoppingListRepository.findByIdAndUserId(shoppingListItemDto.getId(), user.getId())).thenReturn(Optional.of(shoppingListItem));

        ArgumentCaptor<InventoryItemDto> argumentCaptor = ArgumentCaptor.forClass(InventoryItemDto.class);

        shoppingListService.purchaseIngredient(shoppingListItemDto);

        verify(shoppingListRepository).delete(shoppingListItem);
        verify(inventoryService).createInventoryItem(argumentCaptor.capture());

        assertEquals(shoppingListItemDto.getIngredient(), argumentCaptor.getValue().getIngredient());
        assertEquals(shoppingListItemDto.getQuantity(), argumentCaptor.getValue().getQuantity());
    }
}
