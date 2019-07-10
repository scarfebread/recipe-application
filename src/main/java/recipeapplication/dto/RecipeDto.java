package recipeapplication.dto;

import java.util.List;

public class RecipeDto
{
    private Long id;
    private String notes;
    private Long rating;
    private Long serves;
    private String cookTime;
    private String prepTime;
    private String difficulty;
    private String newUser;
    private List<IngredientDto> ingredients;
    private List<String> steps;

    public Long getId()
    {
        return id;
    }

    public String getNotes()
    {
        return notes;
    }

    public Long getRating()
    {
        return rating;
    }

    public Long getServes()
    {
        return serves;
    }

    public String getCookTime()
    {
        return cookTime;
    }

    public String getPrepTime()
    {
        return prepTime;
    }

    public String getDifficulty()
    {
        return difficulty;
    }

    public String getNewUser()
    {
        return newUser;
    }

    public List<IngredientDto> getIngredients()
    {
        return ingredients;
    }

    public List<String> getSteps()
    {
        return steps;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setNewUser(String newUser)
    {
        this.newUser = newUser;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public void setRating(Long rating)
    {
        this.rating = rating;
    }

    public void setServes(Long serves)
    {
        this.serves = serves;
    }

    public void setCookTime(String cookTime)
    {
        this.cookTime = cookTime;
    }

    public void setPrepTime(String prepTime)
    {
        this.prepTime = prepTime;
    }

    public void setDifficulty(String difficulty)
    {
        this.difficulty = difficulty;
    }

    public void setIngredients(List<IngredientDto> ingredients)
    {
        this.ingredients = ingredients;
    }

    public void setSteps(List<String> steps)
    {
        this.steps = steps;
    }
}
