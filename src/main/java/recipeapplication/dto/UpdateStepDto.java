package recipeapplication.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateStepDto
{
    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    private Long recipe;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

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
