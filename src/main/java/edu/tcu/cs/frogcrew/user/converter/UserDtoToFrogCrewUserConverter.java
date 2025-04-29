package edu.tcu.cs.frogcrew.user.converter;

import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToFrogCrewUserConverter implements Converter<UserDto, FrogCrewUser> {

    @Override
    public FrogCrewUser convert(UserDto source) {
        FrogCrewUser user = new FrogCrewUser();
        user.setId(source.id());
        user.setFirstName(source.firstName());
        user.setLastName(source.lastName());
        user.setEmail(source.email());
        user.setPhoneNumber(source.phoneNumber());
        user.setRole(source.role());
        user.setQualifiedPositions(source.positions());
        return user;
    }
}
