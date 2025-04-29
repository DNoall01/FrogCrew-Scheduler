package edu.tcu.cs.frogcrew.availability.converter;

import edu.tcu.cs.frogcrew.availability.Availability;
import edu.tcu.cs.frogcrew.availability.dto.AvailabilityScheduleDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityToAvailabilityScheduleDtoConverter implements Converter<Availability, AvailabilityScheduleDto> {

    @Override
    public AvailabilityScheduleDto convert(Availability source) {
        return new AvailabilityScheduleDto(source.getUser().getId(), source.getGame().getSchedule().getId(), source.isAvailable(), source.getComment());
    }
}
