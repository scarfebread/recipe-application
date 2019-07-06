package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipeapplication.model.ShoppingListItem;

import java.util.List;

public interface ShoppingListRepository extends JpaRepository<ShoppingListItem, Long>
{
    List<ShoppingListItem> findByUserId(Long userId);
}
