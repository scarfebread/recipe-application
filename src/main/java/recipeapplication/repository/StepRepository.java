package recipeapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import recipeapplication.model.Step;

import java.util.Optional;

public interface StepRepository extends JpaRepository<Step, Long>
{
    Optional<Step> findById(Long id);
}
