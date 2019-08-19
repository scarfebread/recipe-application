package recipeapplication.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateStepDto
{
    @NotNull
    @NotEmpty
    private String description;

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

    public Long getRecipe()
    {
        return recipe;
    }

    public void setRecipe(Long recipe)
    {
        this.recipe = recipe;
    }
}
