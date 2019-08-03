package recipeapplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import recipeapplication.dto.InventoryItemDto;
import recipeapplication.exception.InventoryItemNotFoundException;
import recipeapplication.model.Ingredient;
import recipeapplication.model.InventoryItem;
import recipeapplication.model.User;
import recipeapplication.repository.IngredientRepository;
import recipeapplication.repository.InventoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class InventoryServiceTest
{
    private InventoryRepository inventoryRepository;
    private InventoryService inventoryService;
    private IngredientRepository ingredientRepository;
    private User user;

    @Before
    public void setup()
    {
        AuthService authService = mock(AuthService.class);
        inventoryRepository = mock(InventoryRepository.class);
        ingredientRepository = mock(IngredientRepository.class);

        user = new User();
        user.setId(12345L);

        when(authService.getLoggedInUser()).thenReturn(user);

        inventoryService = new InventoryService(inventoryRepository, ingredientRepository, authService);
    }

    @Test
    public void shouldGetInventoryItemsForUser()
    {
        List<InventoryItem> inventoryItems = new ArrayList<>();

        inventoryItems.add(new InventoryItem());
        inventoryItems.add(new InventoryItem());

        when(inventoryRepository.findByUser(user)).thenReturn(inventoryItems);

        assertEquals(inventoryItems, inventoryService.getInventory());
    }

    @Test(expected = InventoryItemNotFoundException.class)
    public void shouldThrowInventoryItemNotFoundExceptionWhenInventoryItemDoesNotExist() throws Exception
    {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();
        inventoryItemDto.setId(54321L);

        when(inventoryRepository.findByIdAndUser(inventoryItemDto.getId(), user)).thenReturn(Optional.empty());

        inventoryService.deleteInventoryItem(inventoryItemDto);
    }

    @Test
    public void shouldDeleteInventoryItemWhenInventoryItemExistsForUser() throws Exception
    {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();
        inventoryItemDto.setId(43432L);

        InventoryItem inventoryItem = new InventoryItem();

        when(inventoryRepository.findByIdAndUser(inventoryItemDto.getId(), user)).thenReturn(Optional.of(inventoryItem));

        inventoryService.deleteInventoryItem(inventoryItemDto);

        verify(inventoryRepository).delete(inventoryItem);
    }

    @Test
    public void shouldCreateRecipe()
    {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();
        inventoryItemDto.setIngredient("INGREDIENT");
        inventoryItemDto.setQuantity("QUANTITY");

        when(inventoryRepository.save(any(InventoryItem.class))).then(returnsFirstArg());

        InventoryItem inventoryItem = inventoryService.createInventoryItem(inventoryItemDto);

        Ingredient ingredient = inventoryItem.getIngredient();

        assertEquals(inventoryItemDto.getIngredient(), ingredient.getDescription());
        assertEquals(inventoryItemDto.getQuantity(), ingredient.getImperial());
        assertEquals(inventoryItemDto.getQuantity(), ingredient.getMetric());
    }

    @Test
    public void shouldNotCreateInventoryItemFromIngredientIfAlreadyExists()
    {
        Ingredient ingredient = new Ingredient("INGREDIENT", "QUANTITY", user);

        when(inventoryRepository.findByIngredientAndUser(ingredient, user)).thenReturn(Optional.of(new InventoryItem()));

        inventoryService.createInventoryItem(ingredient);

        verify(inventoryRepository, never()).save(any());
    }

    @Test
    public void shouldCreateInventoryItemFromIngredient()
    {
        Ingredient ingredient = new Ingredient("INGREDIENT", "QUANTITY", user);

        ArgumentCaptor<InventoryItem> argumentCaptor = ArgumentCaptor.forClass(InventoryItem.class);

        inventoryService.createInventoryItem(ingredient);

        verify(inventoryRepository).save(argumentCaptor.capture());

        InventoryItem result = argumentCaptor.getValue();

        assertEquals(ingredient, result.getIngredient());
        assertEquals(user, result.getUser());
    }

    @Test
    public void shouldReturnIngredientsSuccessfully()
    {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("INGREDIENT1");
        ingredients.add("INGREDIENT2");

        when(ingredientRepository.getIngredients(user)).thenReturn(ingredients);

        assertEquals(ingredients, inventoryService.getIngredients());
    }

    @Test
    public void shouldGetSimilarIngredients()
    {
        Ingredient ingredient1 = new Ingredient();
        Ingredient ingredient2 = new Ingredient();
        InventoryItem inventoryItem1 = new InventoryItem();
        InventoryItem inventoryItem2 = new InventoryItem();
        inventoryItem1.setIngredient(ingredient1);
        inventoryItem2.setIngredient(ingredient2);
        List<InventoryItem> inventoryItems = new ArrayList<>();
        inventoryItems.add(inventoryItem1);
        inventoryItems.add(inventoryItem2);

        Ingredient searchIngredient = new Ingredient("DESCRIPTION", null, null);

        when(inventoryRepository.findByIngredientDescription(searchIngredient.getDescription())).thenReturn(inventoryItems);

        List<Ingredient> result = inventoryService.getSimilarIngredients(searchIngredient);

        assertEquals(ingredient1, result.get(0));
        assertEquals(ingredient2, result.get(1));
    }

    @Test
    public void shouldGetSimilarIngredientsWithNoResults()
    {
        Ingredient searchIngredient = new Ingredient("DESCRIPTION", null, null);

        when(inventoryRepository.findByIngredientDescription(searchIngredient.getDescription())).thenReturn(new ArrayList<>());

        List<Ingredient> result = inventoryService.getSimilarIngredients(searchIngredient);

        assertEquals(0, result.size());
    }
}
