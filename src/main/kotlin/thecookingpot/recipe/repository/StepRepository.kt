package thecookingpot.recipe.repository

import org.springframework.data.jpa.repository.JpaRepository
import thecookingpot.recipe.model.Step

interface StepRepository : JpaRepository<Step, Long>