package recipeapplication.dto;

import javax.validation.constraints.NotNull;

public class DeleteStepDto
{
    @NotNull
    private Long stepId;

    @NotNull
    private Long recipeId;

    public Long getStepId()
    {
        return stepId;
    }

    public void setStepId(Long stepId)
    {
        this.stepId = stepId;
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
