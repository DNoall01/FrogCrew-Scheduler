package edu.tcu.cs.frogcrew.position.converter;

import edu.tcu.cs.frogcrew.position.Position;
import edu.tcu.cs.frogcrew.position.dto.PositionDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PositionDtoToPositionConverter implements Converter<PositionDto, Position> {

    @Override
    public Position convert(PositionDto source) {
        Position position = new Position();
        position.setPositionId(source.positionId());
        position.setPositionName(source.positionName());
        return position;
    }
}
