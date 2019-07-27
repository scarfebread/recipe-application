package recipeapplication.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ShoppingListItemDto
{
    private Long id;

    @NotNull
    @NotEmpty
    private String ingredient;

    private String quantity;

    private Long inventoryItemId;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getIngredient()
    {
        return ingredient;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setIngredient(String ingredient)
    {
        this.ingredient = ingredient;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }

    public Long getInventoryItemId()
    {
        return inventoryItemId;
    }

    public void setInventoryItemId(Long inventoryItemId)
    {
        this.inventoryItemId = inventoryItemId;
    }
}
