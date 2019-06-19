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

    private String metric;
    private String imperial;

    public Ingredient(Recipe recipe, String ingredient)
    {
        this.recipe = recipe;
        this.metric = IngredientConverter.toMetric(ingredient);
        this.imperial = IngredientConverter.toImperial(ingredient);
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
}
