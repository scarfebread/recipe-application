package recipeapplication.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "recipes")
public class Recipe
{
    @Id
    private Long id;
    private String name;
}
