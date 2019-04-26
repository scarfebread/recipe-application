package recipeapplication.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateRecipeDto
{
    @NotNull
    @NotEmpty
    private String title;

    public String getTitle()
    {
        return title;
    }

    public void setName(String name)
    {
        this.title = name;
    }
}
