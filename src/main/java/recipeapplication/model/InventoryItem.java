package recipeapplication.model;

import javax.persistence.*;

@Entity
@Table(name = "inventory")
public class InventoryItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @OneToOne
    private Ingredient ingredient;

    public Long getId()
    {
        return id;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Ingredient getIngredient()
    {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient)
    {
        this.ingredient = ingredient;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
