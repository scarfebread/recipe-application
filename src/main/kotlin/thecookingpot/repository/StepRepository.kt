package thecookingpot.repository

import org.springframework.data.jpa.repository.JpaRepository
import thecookingpot.model.Step

interface StepRepository : JpaRepository<Step, Long>