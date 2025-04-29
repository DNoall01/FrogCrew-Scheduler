package edu.tcu.cs.frogcrew.gametypeproperties;

import edu.tcu.cs.frogcrew.gametypeproperties.converter.GameTypePropertiesDtoToGameTypePropertiesConverter;
import edu.tcu.cs.frogcrew.gametypeproperties.dto.GameTypePropertiesDto;
import edu.tcu.cs.frogcrew.system.exception.ObjectAlreadyExistsException;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GameTypePropertiesService {

    private final GameTypePropertiesRepository gameTypePropertiesRepository;

    public GameTypePropertiesService(GameTypePropertiesRepository gameTypePropertiesRepository) {
        this.gameTypePropertiesRepository = gameTypePropertiesRepository;
    }

    public GameTypeProperties addGameTypeProperties(GameTypeProperties gameTypeProperties) {
        Optional<GameTypeProperties> found = this.gameTypePropertiesRepository.findByPositionIdAndAndGameType(gameTypeProperties.getPositionId(), gameTypeProperties.getGameType());
        if (found.isPresent()) {
            throw new ObjectAlreadyExistsException("position property", "game type", gameTypeProperties.getGameType());
        }
        return this.gameTypePropertiesRepository.save(gameTypeProperties);
    }

    public GameTypeProperties updateGameTypeProperties(Integer positionId, String gameType, GameTypeProperties gameTypeProperties) {
        return this.gameTypePropertiesRepository.findByPositionIdAndAndGameType(positionId, gameType)
                .map(oldProperties -> {
                    oldProperties.setGameType(gameTypeProperties.getGameType());
                    oldProperties.setPayRate(gameTypeProperties.getPayRate());
                    oldProperties.setReportTime(gameTypeProperties.getReportTime());
                    return oldProperties;
                }).orElseThrow(() -> new ObjectNotFoundException("position property", "position", 1, "game type", gameTypeProperties.getGameType()));
    }

    public List<GameTypeProperties> findGameTypePropertiesByGameType(String gameType) {
        return this.gameTypePropertiesRepository.findByGameType(gameType);
    }
}
