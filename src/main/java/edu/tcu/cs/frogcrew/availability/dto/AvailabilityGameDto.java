package edu.tcu.cs.frogcrew.availability.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AvailabilityGameDto(
      @NotNull(message = "User id is required") Integer userId,
      @NotNull(message = "Game id is required") Integer gameId,
      @NotNull(message = "Availability is required") boolean availability,
      String comment) {
}
