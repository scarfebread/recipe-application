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
    private String ingredient;
    private String quantity;

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

    public String getIngredient()
    {
        return ingredient;
    }

    public void setIngredient(String ingredient)
    {
        this.ingredient = ingredient;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }
}
