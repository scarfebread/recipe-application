package recipeapplication.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class IngredientDto
{
    @NotNull
    @NotEmpty
    private String description;

    private String quantity;

    @NotNull
    private Long recipe;

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }

    public Long getRecipe()
    {
        return recipe;
    }

    public void setRecipe(Long recipe)
    {
        this.recipe = recipe;
    }
}
