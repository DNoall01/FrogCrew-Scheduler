package edu.tcu.cs.frogcrew.crewschedule.converter;

import edu.tcu.cs.frogcrew.creweduser.converter.CrewedUserToCrewMemberDtoConverter;
import edu.tcu.cs.frogcrew.crewschedule.dto.CrewScheduleDto;
import edu.tcu.cs.frogcrew.game.Game;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GameToCrewScheduleDtoConverter implements Converter<Game, CrewScheduleDto> {

    private CrewedUserToCrewMemberDtoConverter crewedUserToCrewMemberDtoConverter;

    public GameToCrewScheduleDtoConverter(CrewedUserToCrewMemberDtoConverter crewedUserToCrewMemberDtoConverter) {
        this.crewedUserToCrewMemberDtoConverter = crewedUserToCrewMemberDtoConverter;
    }

    @Override
    public CrewScheduleDto convert(Game source) {
        return new CrewScheduleDto(
                source.getGameId(),
                source.getGameTime(),
                source.getGameDate(),
                source.getVenue(),
                source.getOpponent(),
                source.getCrewMembers().stream().map(crewedUserToCrewMemberDtoConverter::convert).toList()
        );
    }
}
