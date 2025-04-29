package edu.tcu.cs.frogcrew.gametypeproperties;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameTypePropertiesRepository extends JpaRepository<GameTypeProperties, Integer> {

    Optional<GameTypeProperties> findByPositionIdAndAndGameType(Integer positionId, String gameType);

    List<GameTypeProperties> findByGameType(String gameType);
}
