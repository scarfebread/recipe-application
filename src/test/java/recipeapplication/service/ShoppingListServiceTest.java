package recipeapplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import recipeapplication.dto.ShoppingListItemDto;
import recipeapplication.exception.ShoppingListItemNotFoundException;
import recipeapplication.model.Ingredient;
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
    private User user;

    @Before
    public void setup()
    {
        AuthService authService = mock(AuthService.class);

        user = new User();
        user.setId(12345L);

        when(authService.getLoggedInUser()).thenReturn(user);

        shoppingListRepository = mock(ShoppingListRepository.class);

        shoppingListService = new ShoppingListService(shoppingListRepository, authService);
    }

    @Test
    public void shouldGetShoppingListItemsForUser()
    {
        List<ShoppingListItem> shoppingListItems = new ArrayList<>();

        shoppingListItems.add(new ShoppingListItem());
        shoppingListItems.add(new ShoppingListItem());

        when(shoppingListRepository.findByUser(user)).thenReturn(shoppingListItems);

        assertEquals(shoppingListItems, shoppingListService.getShoppingList());
    }

    @Test(expected = ShoppingListItemNotFoundException.class)
    public void shouldThrowShoppingListItemNotFoundExceptionWhenShoppingListItemDoesNotExist() throws Exception
    {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();
        shoppingListItemDto.setId(54321L);

        when(shoppingListRepository.findByIdAndUser(shoppingListItemDto.getId(), user)).thenReturn(Optional.empty());

        shoppingListService.deleteShoppingListItem(shoppingListItemDto);
    }

    @Test
    public void shouldDeleteShoppingListItemWhenShoppingListItemExistsForUser() throws Exception
    {
        ShoppingListItemDto shoppingListItemDto = new ShoppingListItemDto();
        shoppingListItemDto.setId(124542L);

        ShoppingListItem shoppingListItem = new ShoppingListItem();

        when(shoppingListRepository.findByIdAndUser(shoppingListItemDto.getId(), user)).thenReturn(Optional.of(shoppingListItem));

        shoppingListService.deleteShoppingListItem(shoppingListItemDto);

        verify(shoppingListRepository).delete(shoppingListItem);
    }

    @Test(expected = ShoppingListItemNotFoundException.class)
    public void shouldThrowShoppingListItemNotFoundExceptionWhenDeletingUsingInventoryItem() throws Exception
    {
        Ingredient ingredient = new Ingredient();
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setIngredient(ingredient);

        when(shoppingListRepository.findByIngredientAndUser(ingredient, user)).thenReturn(Optional.empty());

        shoppingListService.deleteShoppingListItem(inventoryItem);
    }

    @Test
    public void shouldDeleteShoppingListItemWhenUsingExistingInventoryItem() throws Exception
    {
        Ingredient ingredient = new Ingredient();
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setIngredient(ingredient);

        ShoppingListItem shoppingListItem = new ShoppingListItem();

        when(shoppingListRepository.findByIngredientAndUser(ingredient, user)).thenReturn(Optional.of(shoppingListItem));

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

        Ingredient ingredient = shoppingListItem.getIngredient();

        assertEquals(shoppingListItemDto.getIngredient(), ingredient.getDescription());
        assertEquals(shoppingListItemDto.getQuantity(), ingredient.getMetric());
        assertEquals(shoppingListItemDto.getQuantity(), ingredient.getImperial());
    }

    @Test
    public void shouldCreateRecipeFromInventoryItem()
    {
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setIngredient(new Ingredient());

        ArgumentCaptor<ShoppingListItem> argumentCaptor = ArgumentCaptor.forClass(ShoppingListItem.class);

        shoppingListService.createShoppingListItem(inventoryItem);

        verify(shoppingListRepository).save(argumentCaptor.capture());

        assertEquals(inventoryItem.getIngredient(), argumentCaptor.getValue().getIngredient());
    }
}
