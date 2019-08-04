package recipeapplication.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private boolean inShoppingList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private List<Ingredient> inventoryItems;

    @OneToOne
    private User user;

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

    public Long getId()
    {
        return id;
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

    public boolean isInShoppingList()
    {
        return inShoppingList;
    }

    public void setInShoppingList(boolean inShoppingList)
    {
        this.inShoppingList = inShoppingList;
    }

    public List<Ingredient> getInventoryItems()
    {
        return inventoryItems;
    }

    public void setInventoryItems(List<Ingredient> inventoryItems)
    {
        this.inventoryItems = inventoryItems;
    }
}
