package recipeapplication.model;

import javax.persistence.*;

@Entity
@Table(name = "recentlyViewed")
public class RecentlyViewed
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe")
    private Recipe recipe;

    @OneToOne
    private User user;

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Recipe getRecipe()
    {
        return recipe;
    }

    public void setRecipe(Recipe recipe)
    {
        this.recipe = recipe;
    }
}
