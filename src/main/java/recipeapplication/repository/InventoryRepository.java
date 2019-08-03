package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipeapplication.model.Ingredient;
import recipeapplication.model.InventoryItem;
import recipeapplication.model.User;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long>
{
    List<InventoryItem> findByUser(User user);
    Optional<InventoryItem> findByIdAndUser(Long id, User user);
    Optional<InventoryItem> findByIngredientAndUser(Ingredient ingredient, User user);
    List<InventoryItem> findByIngredientDescription(String ingredient);
}
