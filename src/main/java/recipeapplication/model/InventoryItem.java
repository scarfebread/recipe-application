package recipeapplication.model;

import javax.persistence.*;

@Entity
@Table(name = "inventory")
public class InventoryItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToOne
    private Ingredient ingredient;

    @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id",insertable = false, updatable = false)
    @OneToOne
    private ShoppingListItem shoppingListItem;

    public Long getId()
    {
        return id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
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

    public ShoppingListItem getShoppingListItem()
    {
        return shoppingListItem;
    }
}
