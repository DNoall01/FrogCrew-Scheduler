package edu.tcu.cs.frogcrew.user.converter;

import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FrogCrewUserToUserDtoConverter implements Converter<FrogCrewUser, UserDto> {

    @Override
    public UserDto convert(FrogCrewUser source) {
        return new UserDto(
                source.getId(),
                source.getFirstName(),
                source.getLastName(),
                source.getEmail(),
                source.getPhoneNumber(),
                source.getRole(),
                source.getQualifiedPositions()
        );
    }
}
