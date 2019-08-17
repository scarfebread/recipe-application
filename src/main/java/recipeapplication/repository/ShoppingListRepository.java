package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipeapplication.model.Ingredient;
import recipeapplication.model.ShoppingListItem;
import recipeapplication.model.User;

import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository extends JpaRepository<ShoppingListItem, Long>
{
    List<ShoppingListItem> findByUser(User user);
    Optional<ShoppingListItem> findByIdAndUser(Long id, User user);
    Optional<ShoppingListItem> findByIngredientIdAndUser(Long ingredientId, User user);
    Optional<ShoppingListItem> findByIngredient(Ingredient ingredient);
    void deleteAllByUser(User user);
}
