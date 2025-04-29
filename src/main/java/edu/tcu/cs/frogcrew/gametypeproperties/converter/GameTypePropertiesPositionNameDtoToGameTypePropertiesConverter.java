package edu.tcu.cs.frogcrew.gametypeproperties.converter;

import edu.tcu.cs.frogcrew.gametypeproperties.GameTypeProperties;
import edu.tcu.cs.frogcrew.gametypeproperties.dto.GameTypePropertiesPositionNameDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameTypePropertiesPositionNameDtoToGameTypePropertiesConverter implements Converter<GameTypePropertiesPositionNameDto, GameTypeProperties> {

    @Override
    public GameTypeProperties convert(GameTypePropertiesPositionNameDto source) {
        GameTypeProperties gameTypeProperties = new GameTypeProperties();
        gameTypeProperties.setPositionId(source.positionId());
        gameTypeProperties.setPayRate(source.properties().getPayRateProperty());
        gameTypeProperties.setReportTime(source.properties().getReportTimeProperty());
        return gameTypeProperties;
    }
}
