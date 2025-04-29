// src/main/java/edu/tcu/cs/frogcrew/game/converter/GameToGameDtoConverter.java
package edu.tcu.cs.frogcrew.game.converter;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.dto.GameDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GameToGameDtoConverter implements Converter<Game, GameDto> {

    @Override
    public GameDto convert(Game source) {
        // grab schedule ID if present
        Integer schedId = source.getSchedule() != null
                          ? source.getSchedule().getId()
                          : null;

        // protect against null positions list
        List<String> positions = source.getPositions() != null
                                 ? source.getPositions()
                                 : Collections.emptyList();

        return new GameDto(
            source.getGameId(),
            schedId,
            source.getGameDateTime(),
            source.getVenue(),
            source.getOpponent(),
            source.isFinalized(),
            positions            // <-- make sure you pass this!
        );
    }
}
