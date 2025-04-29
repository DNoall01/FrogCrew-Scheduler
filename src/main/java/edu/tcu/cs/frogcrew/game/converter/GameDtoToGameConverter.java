package edu.tcu.cs.frogcrew.game.converter;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.dto.GameDto;
import edu.tcu.cs.frogcrew.schedule.ScheduleRepository;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GameDtoToGameConverter implements Converter<GameDto, Game> {

    private final ScheduleRepository scheduleRepository;

    public GameDtoToGameConverter(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Game convert(GameDto source) {

        Game game = new Game();

        /* ID (for updates) */
        if (source.gameId() != null) {
            game.setGameId(source.gameId());
        }

        /* parent schedule */
        game.setSchedule(
            scheduleRepository.findById(source.scheduleId())
                .orElseThrow(() ->
                     new ObjectNotFoundException("schedule", source.scheduleId()))
        );

        /* simple fields */
        game.setGameDateTime(source.gameDate());
        game.setVenue(source.venue());
        game.setOpponent(source.opponent());
        game.setFinalized(source.finalized());

        /* NEW: copy the positions list (null-safe) */
        List<String> positions = source.requiredPositions() != null
                                 ? source.requiredPositions()
                                 : Collections.emptyList();
        game.setPositions(positions);

        return game;
    }
}
