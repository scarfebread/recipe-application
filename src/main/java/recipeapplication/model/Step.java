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

    private String name;
    private int step;

    public Step() {}

    public Step(Recipe recipe, String name)
    {
        this.recipe = recipe;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
