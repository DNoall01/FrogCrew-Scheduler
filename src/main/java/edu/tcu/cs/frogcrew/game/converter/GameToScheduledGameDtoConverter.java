package edu.tcu.cs.frogcrew.game.converter;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.dto.ScheduledGameDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameToScheduledGameDtoConverter implements Converter<Game, ScheduledGameDto> {

    @Override
    public ScheduledGameDto convert(Game source) {
        return new ScheduledGameDto(
                source.getGameId(),
                source.getGameDate(),
                source.getVenue(),
                source.getOpponent(),
                source.isFinalized()
        );
    }
}
