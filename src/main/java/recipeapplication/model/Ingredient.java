package recipeapplication.model;

import recipeapplication.utility.IngredientConverter;

import javax.persistence.*;

@Entity
@Table(name = "ingredients")
public class Ingredient
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe")
    private Recipe recipe;

    private String description;
    private String metric;
    private String imperial;

    public Ingredient(Recipe recipe, String description, String quantity)
    {
        this.recipe = recipe;
        this.description = description;
        this.metric = IngredientConverter.toMetric(quantity);
        this.imperial = IngredientConverter.toImperial(quantity);
    }

    public Ingredient() {}

    public String getMetric()
    {
        return metric;
    }

    public String getImperial()
    {
        return imperial;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setMetric(String metric)
    {
        this.metric = metric;
    }

    public void setImperial(String imperial)
    {
        this.imperial = imperial;
    }

    public void setRecipe(Recipe recipe)
    {
        this.recipe = recipe;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
