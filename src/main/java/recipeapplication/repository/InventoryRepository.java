package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipeapplication.model.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long>
{
    List<InventoryItem> findByUserId(Long userId);
    Optional<InventoryItem> findByIdAndUserId(Long id, Long userId);
}
