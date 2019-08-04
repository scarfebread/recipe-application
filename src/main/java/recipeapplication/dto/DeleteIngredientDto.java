package recipeapplication.dto;

import javax.validation.constraints.NotNull;

public class DeleteIngredientDto
{
    @NotNull
    private Long ingredientId;

    @NotNull
    private Long recipeId;

    public Long getIngredientId()
    {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId)
    {
        this.ingredientId = ingredientId;
    }

    public Long getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(Long recipeId)
    {
        this.recipeId = recipeId;
    }
}
