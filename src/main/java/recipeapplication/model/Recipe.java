package recipeapplication.model;

import javax.persistence.*;
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
    private Long userId;

    @OneToMany(mappedBy = "recipe")
    private List<Ingredient> ingredients;

    @OneToMany(mappedBy = "recipe")
    private List<Step> steps;

    public Long getUserId()
    {
        return userId;
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

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public void setIngredients(List<Ingredient> ingredients)
    {
        this.ingredients = ingredients;
    }

    public void setSteps(List<Step> steps)
    {
        this.steps = steps;
    }
}
