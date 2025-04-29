package edu.tcu.cs.frogcrew.gametypeproperties.dto;

import edu.tcu.cs.frogcrew.gametypeproperties.Properties;

public record GameTypePropertiesDto(
        Integer positionId,
        String gameType,
        Properties properties
) {
}
