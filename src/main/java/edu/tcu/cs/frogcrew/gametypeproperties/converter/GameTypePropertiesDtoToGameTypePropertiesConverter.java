package edu.tcu.cs.frogcrew.gametypeproperties.converter;

import edu.tcu.cs.frogcrew.gametypeproperties.GameTypeProperties;
import edu.tcu.cs.frogcrew.gametypeproperties.dto.GameTypePropertiesDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameTypePropertiesDtoToGameTypePropertiesConverter implements Converter<GameTypePropertiesDto, GameTypeProperties> {

    @Override
    public GameTypeProperties convert(GameTypePropertiesDto source) {
        GameTypeProperties gameTypeProperties = new GameTypeProperties();
        gameTypeProperties.setPositionId(source.positionId());
        gameTypeProperties.setGameType(source.gameType());

        if (source.properties() != null) {
            gameTypeProperties.setPayRate(source.properties().getPayRateProperty());
            gameTypeProperties.setReportTime(source.properties().getReportTimeProperty());
        }
        return gameTypeProperties;
    }
}
