package recipeapplication.model;

import javax.persistence.*;

@Entity
@Table(name = "shopping_list")
public class ShoppingListItem
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Ingredient ingredient;

    private Long inventoryId;

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
}
