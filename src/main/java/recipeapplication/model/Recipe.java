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
}
