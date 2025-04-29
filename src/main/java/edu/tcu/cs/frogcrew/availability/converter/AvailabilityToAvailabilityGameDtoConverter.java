package edu.tcu.cs.frogcrew.availability.converter;

import edu.tcu.cs.frogcrew.availability.Availability;
import edu.tcu.cs.frogcrew.availability.dto.AvailabilityGameDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityToAvailabilityGameDtoConverter implements Converter<Availability, AvailabilityGameDto> {

    @Override
    public AvailabilityGameDto convert(Availability source) {
        return new AvailabilityGameDto(source.getUser().getId(), source.getGame().getGameId(), source.isAvailable(), source.getComment());
    }

}
