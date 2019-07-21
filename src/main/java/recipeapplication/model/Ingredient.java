package recipeapplication.model;

import recipeapplication.utility.IngredientConverter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ingredients")
public class Ingredient
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String metric;
    private String imperial;

    @OneToOne
    private User user;

    @ManyToMany(mappedBy = "ingredients")
    private List<Recipe> recipe;

    public Ingredient(String description, String quantity, User user)
    {
        this.description = description;
        this.metric = IngredientConverter.toMetric(quantity);
        this.imperial = IngredientConverter.toImperial(quantity);
        this.user = user;
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

    public String getDescription()
    {
        return description;
    }

    public User getUser()
    {
        return user;
    }
}
