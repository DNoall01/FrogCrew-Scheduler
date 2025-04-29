package edu.tcu.cs.frogcrew.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FrogCrewUserRepository extends JpaRepository<FrogCrewUser, Integer> {
    List<FrogCrewUser> findFrogCrewUsersByQualifiedPositionsContainingIgnoreCase(String position);
    Optional<FrogCrewUser> findByUsername(String username);

}
