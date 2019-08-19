package recipeapplication.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes")
public class Recipe
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String sharedBy;
    private Long rating;
    private String notes;
    private Long serves;
    private String cookTime;
    private String prepTime;
    private String totalTime;
    private String difficulty;

    @OneToOne
    private User user;

    @JoinTable(
            name = "recipe_ingredients",
            joinColumns = @JoinColumn(
                    name = "recipe",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "ingredient",
                    referencedColumnName = "id"
            )
    )
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Step> steps;

    // TODO this isn't used. Should it be?
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecentlyViewed> recentlyViewed;

    public User getUser()
    {
        return user;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setSharedBy(String sharedBy)
    {
        this.sharedBy = sharedBy;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void setIngredients(List<Ingredient> ingredients)
    {
        this.ingredients = ingredients;
    }

    public void setSteps(List<Step> steps)
    {
        this.steps = steps;
    }

    public Long getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSharedBy()
    {
        return sharedBy;
    }

    public List<Ingredient> getIngredients()
    {
        return ingredients;
    }

    public List<Step> getSteps()
    {
        return steps;
    }

    public Long getRating()
    {
        return rating;
    }

    public void setRating(Long rating)
    {
        this.rating = rating;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public Long getServes()
    {
        return serves;
    }

    public void setServes(Long serves)
    {
        this.serves = serves;
    }

    public String getCookTime()
    {
        return cookTime;
    }

    public void setCookTime(String cookTime)
    {
        this.cookTime = cookTime;
    }

    public String getPrepTime()
    {
        return prepTime;
    }

    public void setPrepTime(String prepTime)
    {
        this.prepTime = prepTime;
    }

    public String getDifficulty()
    {
        return difficulty;
    }

    public void setDifficulty(String difficulty)
    {
        this.difficulty = difficulty;
    }

    public String getTotalTime()
    {
        return totalTime;
    }

    public void setTotalTime(String totalTime)
    {
        this.totalTime = totalTime;
    }

    public void addIngredient(Ingredient ingredient)
    {
        if (ingredients == null)
        {
            ingredients = new ArrayList<>();
        }

        ingredients.add(ingredient);
    }

    public void addStep(Step step)
    {
        if (steps == null)
        {
            steps = new ArrayList<>();
        }

        steps.add(step);
    }
}
