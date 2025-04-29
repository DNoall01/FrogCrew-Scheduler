package edu.tcu.cs.frogcrew.position.dto;

import jakarta.validation.constraints.NotEmpty;

public record PositionDto(Integer positionId,
                          @NotEmpty(message = "Position is required") String positionName) {
}
