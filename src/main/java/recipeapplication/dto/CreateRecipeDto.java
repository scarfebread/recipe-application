package recipeapplication.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateRecipeDto
{
    @NotNull
    @NotEmpty
    private String name;

    public String getTitle()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
