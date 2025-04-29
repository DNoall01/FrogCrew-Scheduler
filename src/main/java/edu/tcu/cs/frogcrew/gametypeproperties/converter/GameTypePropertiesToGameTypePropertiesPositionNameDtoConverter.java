package edu.tcu.cs.frogcrew.gametypeproperties.converter;

import edu.tcu.cs.frogcrew.gametypeproperties.GameTypeProperties;
import edu.tcu.cs.frogcrew.gametypeproperties.Properties;
import edu.tcu.cs.frogcrew.gametypeproperties.dto.GameTypePropertiesPositionNameDto;
import edu.tcu.cs.frogcrew.position.Position;
import edu.tcu.cs.frogcrew.position.PositionRepository;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameTypePropertiesToGameTypePropertiesPositionNameDtoConverter implements Converter<GameTypeProperties, GameTypePropertiesPositionNameDto> {

    private final PositionRepository positionRepository;

    public GameTypePropertiesToGameTypePropertiesPositionNameDtoConverter(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public GameTypePropertiesPositionNameDto convert(GameTypeProperties source) {

        Position position = this.positionRepository.findById(source.getPositionId()).orElseThrow(() -> new ObjectNotFoundException("position", source.getPositionId()));

        return new GameTypePropertiesPositionNameDto(
                source.getPositionId(),
                position.getPositionName(),
                new Properties(source.getPayRate(), source.getReportTime())
        );
    }
}
