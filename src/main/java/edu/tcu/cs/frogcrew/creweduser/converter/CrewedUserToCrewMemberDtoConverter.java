package edu.tcu.cs.frogcrew.creweduser.converter;

import edu.tcu.cs.frogcrew.creweduser.CrewedUser;
import edu.tcu.cs.frogcrew.creweduser.dto.CrewedMemberDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CrewedUserToCrewMemberDtoConverter implements Converter<CrewedUser, CrewedMemberDto> {

    @Override
    public CrewedMemberDto convert(CrewedUser source) {

        return new CrewedMemberDto(
                source.getCrewedUserId(),
                source.getUser().getId(),
                source.getGame().getGameId(),
                source.getUser().getFirstName() + " " + source.getUser().getLastName(),
                source.getPosition().getPositionName(),
                source.getPosition().getGameTypeProperties().getReportTime(),
                source.getPosition().getPositionLocation()
        );
    }
}
