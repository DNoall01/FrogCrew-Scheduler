package edu.tcu.cs.frogcrew.templateposition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplatePositionRepository extends JpaRepository<TemplatePosition, Integer> {
}
