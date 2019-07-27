package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipeapplication.model.Ingredient;
import recipeapplication.model.ShoppingListItem;

import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository extends JpaRepository<ShoppingListItem, Long>
{
    List<ShoppingListItem> findByUserId(Long userId);
    Optional<ShoppingListItem> findByIdAndUserId(Long id, Long userId);
    Optional<ShoppingListItem> findByIngredientAndUserId(Ingredient ingredient, Long userId);
}
