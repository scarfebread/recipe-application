package recipeapplication.model;

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

    private String name;

    public Ingredient(Recipe recipe, String name)
    {
        this.recipe = recipe;
        this.name = name;
    }

    public Ingredient() {}

    public String getName()
    {
        return name;
    }
}
