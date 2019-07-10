package recipeapplication.model;

import javax.persistence.*;

@Entity
@Table(name = "steps")
public class Step
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe")
    private Recipe recipe;

    private String description;
    private int step;

    public Step() {}

    public Step(Recipe recipe, String description)
    {
        this.recipe = recipe;
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
