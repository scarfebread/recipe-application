package recipeapplication.repository

import org.springframework.data.jpa.repository.JpaRepository
import recipeapplication.model.RecentlyViewed
import recipeapplication.model.User

interface RecentlyViewedRepository : JpaRepository<RecentlyViewed, Long?> {
    fun findTop5ByUserOrderByIdDesc(user: User): List<RecentlyViewed>
}