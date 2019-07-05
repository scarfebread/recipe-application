package recipeapplication.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import recipeapplication.dto.InventoryItemDto;
import recipeapplication.exception.InventoryItemNotFoundException;
import recipeapplication.model.InventoryItem;
import recipeapplication.model.User;
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
    private User user;

    @Before
    public void setup()
    {
        AuthService authService = mock(AuthService.class);

        user = new User();
        user.setId(12345L);

        when(authService.getLoggedInUser()).thenReturn(user);

        inventoryRepository = mock(InventoryRepository.class);

        inventoryService = new InventoryService(inventoryRepository, authService);
    }

    @Test
    public void shouldGetInventoryItemsForUser()
    {
        List<InventoryItem> inventoryItems = new ArrayList<>();

        inventoryItems.add(new InventoryItem());
        inventoryItems.add(new InventoryItem());

        when(inventoryRepository.findByUserId(user.getId())).thenReturn(inventoryItems);

        assertEquals(inventoryItems, inventoryService.getInventoryItems());
    }

    @Test(expected = InventoryItemNotFoundException.class)
    public void shouldThrowInventoryItemNotFoundExceptionWhenInventoryItemDoesNotExist() throws Exception
    {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();
        inventoryItemDto.setId(54321L);

        when(inventoryRepository.findByIdAndUserId(inventoryItemDto.getId(), user.getId())).thenReturn(Optional.empty());

        inventoryService.deleteInventoryItem(inventoryItemDto);
    }

    @Test
    public void shouldDeleteInventoryItemWhenInventoryItemExistsForUser() throws Exception
    {
        InventoryItemDto inventoryItemDto = new InventoryItemDto();
        inventoryItemDto.setId(43432L);

        InventoryItem inventoryItem = new InventoryItem();

        when(inventoryRepository.findByIdAndUserId(inventoryItemDto.getId(), user.getId())).thenReturn(Optional.of(inventoryItem));

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

        assertEquals(inventoryItemDto.getIngredient(), inventoryItem.getIngredient());
        assertEquals(inventoryItemDto.getQuantity(), inventoryItem.getQuantity());
    }
}
