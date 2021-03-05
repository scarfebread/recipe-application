package thecookingpot.recipe.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import thecookingpot.recipe.dto.ShoppingListItemDto;
import thecookingpot.recipe.exception.IngredientDoesNotExistException;
import thecookingpot.recipe.exception.ShoppingListItemNotFoundException;
import thecookingpot.recipe.model.Ingredient;
import thecookingpot.recipe.model.ShoppingListItem;
import thecookingpot.recipe.model.User;
import thecookingpot.recipe.repository.IngredientRepository;
import thecookingpot.recipe.repository.ShoppingListRepository;
import thecookingpot.security.service.AuthService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class ShoppingListServiceTest
{
    private ShoppingListRepository shoppingListRepository;
    private ShoppingListService shoppingListService;
    private IngredientRepository ingredientRepository;
    private User user;

    @Before
    public void setup()
    {
        AuthService authService = mock(AuthService.class);

        user = new User();
        user.setId(12345L);

        when(authService.getLoggedInUser()).thenReturn(user);

        shoppingListRepository = mock(ShoppingListRepository.class);
        ingredientRepository = mock(IngredientRepository.class);

        shoppingListService = new ShoppingListService(shoppingListRepository, ingredientRepository, authService);
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

    @Test(expected = IngredientDoesNotExistException.class)
    public void shouldThrowIngredientDoesNotExistExceptionWhenAddingNonExistentIngredient() throws Exception
    {
        Long ingredientId = 12345L;

        when(ingredientRepository.findByIdAndUser(ingredientId, user)).thenReturn(null);

        shoppingListService.addToShoppingList(ingredientId);
    }

    @Test
    public void shouldAddIngredientToShoppingListWhenIngredientExists() throws Exception
    {
        long ingredientId = 12345L;
        Ingredient ingredient = new Ingredient();

        when(ingredientRepository.findByIdAndUser(ingredientId, user)).thenReturn(ingredient);

        ArgumentCaptor<ShoppingListItem> argumentCaptor = ArgumentCaptor.forClass(ShoppingListItem.class);

        shoppingListService.addToShoppingList(ingredientId);

        verify(shoppingListRepository).save(argumentCaptor.capture());

        ShoppingListItem shoppingListItem = argumentCaptor.getValue();

        assertEquals(user, shoppingListItem.getUser());
        assertEquals(ingredient, shoppingListItem.getIngredient());
    }

    @Test(expected = ShoppingListItemNotFoundException.class)
    public void shouldThrowShoppingListItemNotFoundExceptionWhenRemovingNonExistentShoppingListItem() throws Exception
    {
        long ingredientId = 12345L;

        when(shoppingListRepository.findByIngredientIdAndUser(ingredientId, user)).thenReturn(Optional.empty());

        shoppingListService.removeFromShoppingList(ingredientId);
    }

    @Test
    public void shouldRemoveShoppingListItemWhenShoppingListItemExists() throws Exception
    {
        long ingredientId = 12345L;
        ShoppingListItem shoppingListItem = new ShoppingListItem();

        when(shoppingListRepository.findByIngredientIdAndUser(ingredientId, user)).thenReturn(Optional.of(shoppingListItem));

        shoppingListService.removeFromShoppingList(ingredientId);

        verify(shoppingListRepository).delete(shoppingListItem);
    }

    @Test
    public void shouldReturnTrueWhenIngredientInShoppingList()
    {
        Ingredient ingredient = new Ingredient();

        when(shoppingListRepository.findByIngredient(ingredient)).thenReturn(Optional.of(new ShoppingListItem()));

        assertTrue(shoppingListService.isInShoppingList(ingredient));
    }

    @Test
    public void shouldReturnFalseWhenIngredientNotInShoppingList()
    {
        Ingredient ingredient = new Ingredient();

        when(shoppingListRepository.findByIngredient(ingredient)).thenReturn(Optional.empty());

        assertFalse(shoppingListService.isInShoppingList(ingredient));
    }

    @Test
    public void shouldDeleteAllSuccessfully()
    {
        shoppingListService.deleteAll();

        verify(shoppingListRepository).deleteAllByUser(user);
    }
}
