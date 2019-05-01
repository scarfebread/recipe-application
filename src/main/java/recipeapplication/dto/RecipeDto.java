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
    private List<String> ingredients;
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

    public List<String> getIngredients()
    {
        return ingredients;
    }

    public List<String> getSteps()
    {
        return steps;
    }
}
