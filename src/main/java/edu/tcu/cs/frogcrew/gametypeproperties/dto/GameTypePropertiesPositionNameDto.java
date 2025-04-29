package edu.tcu.cs.frogcrew.gametypeproperties.dto;

import edu.tcu.cs.frogcrew.gametypeproperties.Properties;

public record GameTypePropertiesPositionNameDto(
        Integer positionId,
        String positionName,
        Properties properties
) {
}
