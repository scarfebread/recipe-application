package recipeapplication.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class IngredientDto
{
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private Long recipe;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
