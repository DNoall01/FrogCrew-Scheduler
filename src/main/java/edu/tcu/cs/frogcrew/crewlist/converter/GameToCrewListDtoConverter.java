package edu.tcu.cs.frogcrew.crewlist.converter;

import edu.tcu.cs.frogcrew.creweduser.converter.CrewedUserToCrewMemberDtoConverter;
import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.crewlist.dto.CrewListDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameToCrewListDtoConverter implements Converter<Game, CrewListDto> {

    private final CrewedUserToCrewMemberDtoConverter crewedUserToCrewMemberDtoConverter;

    public GameToCrewListDtoConverter(CrewedUserToCrewMemberDtoConverter crewedUserToCrewMemberDtoConverter) {
        this.crewedUserToCrewMemberDtoConverter = crewedUserToCrewMemberDtoConverter;
    }

    @Override
    public CrewListDto convert(Game source) {
        return new CrewListDto(
                source.getGameId(),
                source.getGameDateTime().toLocalTime(),
                source.getGameDateTime().toLocalDate(),
                source.getVenue(),
                source.getOpponent(),
                source.getCrewMembers().stream().map(crewedUserToCrewMemberDtoConverter::convert).toList()
        );
    }
}
