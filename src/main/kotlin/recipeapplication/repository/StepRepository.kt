package recipeapplication.repository

import org.springframework.data.jpa.repository.JpaRepository
import recipeapplication.model.Step

interface StepRepository : JpaRepository<Step, Long>