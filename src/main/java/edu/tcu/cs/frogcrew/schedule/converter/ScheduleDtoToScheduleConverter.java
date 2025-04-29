package edu.tcu.cs.frogcrew.schedule.converter;

import edu.tcu.cs.frogcrew.schedule.Schedule;
import edu.tcu.cs.frogcrew.schedule.dto.ScheduleDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ScheduleDtoToScheduleConverter implements Converter<ScheduleDto, Schedule> {


    @Override
    public Schedule convert(ScheduleDto source) {
        Schedule schedule = new Schedule();
        if (source.id() != null) schedule.setId(source.id());
        schedule.setSeason(source.season());
        schedule.setSport(source.sport());
        return schedule;
    }

}
