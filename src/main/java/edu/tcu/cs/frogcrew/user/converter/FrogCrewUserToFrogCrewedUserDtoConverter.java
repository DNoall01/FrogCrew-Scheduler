package edu.tcu.cs.frogcrew.user.converter;

import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.dto.FrogCrewedUserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FrogCrewUserToFrogCrewedUserDtoConverter implements Converter<FrogCrewUser, FrogCrewedUserDto> {
    @Override
    public FrogCrewedUserDto convert(FrogCrewUser source) {
        return new FrogCrewedUserDto(
                source.getId(),
                source.getFirstName() + " " + source.getLastName()
        );
    }
}
