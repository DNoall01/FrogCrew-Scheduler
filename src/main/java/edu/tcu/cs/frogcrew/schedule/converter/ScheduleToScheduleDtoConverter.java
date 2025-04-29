package edu.tcu.cs.frogcrew.schedule.converter;

import edu.tcu.cs.frogcrew.schedule.Schedule;
import edu.tcu.cs.frogcrew.schedule.dto.ScheduleDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ScheduleToScheduleDtoConverter implements Converter<Schedule, ScheduleDto> {

    @Override
    public ScheduleDto convert(Schedule source) {
        return new ScheduleDto(source.getId(), source.getSport(), source.getSeason());
    }
}
