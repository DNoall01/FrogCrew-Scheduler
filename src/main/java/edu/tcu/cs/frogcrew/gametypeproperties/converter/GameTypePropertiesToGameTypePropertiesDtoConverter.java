package edu.tcu.cs.frogcrew.gametypeproperties.converter;

import edu.tcu.cs.frogcrew.gametypeproperties.GameTypeProperties;
import edu.tcu.cs.frogcrew.gametypeproperties.Properties;
import edu.tcu.cs.frogcrew.gametypeproperties.dto.GameTypePropertiesDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameTypePropertiesToGameTypePropertiesDtoConverter implements Converter<GameTypeProperties, GameTypePropertiesDto> {

    @Override
    public GameTypePropertiesDto convert(GameTypeProperties source) {
        return new GameTypePropertiesDto(
                source.getPositionId(),
                source.getGameType(),
                new Properties(source.getPayRate(), source.getReportTime())
        );
    }
}
