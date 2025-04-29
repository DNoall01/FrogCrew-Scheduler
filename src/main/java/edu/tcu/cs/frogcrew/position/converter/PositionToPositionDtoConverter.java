package edu.tcu.cs.frogcrew.position.converter;

import edu.tcu.cs.frogcrew.position.Position;
import edu.tcu.cs.frogcrew.position.dto.PositionDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PositionToPositionDtoConverter implements Converter<Position, PositionDto> {

    @Override
    public PositionDto convert(Position source) {
        return new PositionDto(source.getPositionId(), source.getPositionName());
    }
}
