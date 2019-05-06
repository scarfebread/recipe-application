package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipeapplication.model.RecentlyViewed;

import java.util.List;

public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewed, Long>
{
    List<RecentlyViewed> findTop5ByUserIdOrderByIdDesc(Long userId);
}
