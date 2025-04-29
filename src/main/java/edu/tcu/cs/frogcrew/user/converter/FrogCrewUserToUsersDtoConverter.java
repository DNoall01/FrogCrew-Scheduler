package edu.tcu.cs.frogcrew.user.converter;

import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.dto.UsersDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FrogCrewUserToUsersDtoConverter implements Converter<FrogCrewUser, UsersDto> {

    @Override
    public UsersDto convert(FrogCrewUser source) {
        return new UsersDto(
                source.getId(),
                source.getFullName(),
                source.getEmail(),
                source.getPhoneNumber()
        );
    }
}
