package thecookingpot.repository

import org.springframework.data.jpa.repository.JpaRepository
import thecookingpot.model.RecentlyViewed
import thecookingpot.model.User

interface RecentlyViewedRepository : JpaRepository<RecentlyViewed, Long?> {
    fun findTop5ByUserOrderByIdDesc(user: User): List<RecentlyViewed>
}