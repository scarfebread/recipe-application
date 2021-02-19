package thecookingpot.recipe.repository

import org.springframework.data.jpa.repository.JpaRepository
import thecookingpot.recipe.model.RecentlyViewed
import thecookingpot.recipe.model.User

interface RecentlyViewedRepository : JpaRepository<RecentlyViewed, Long?> {
    fun findTop5ByUserOrderByIdDesc(user: User): List<RecentlyViewed>
}