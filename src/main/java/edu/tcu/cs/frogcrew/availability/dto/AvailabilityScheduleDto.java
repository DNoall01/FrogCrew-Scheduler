package edu.tcu.cs.frogcrew.availability.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AvailabilityScheduleDto(@NotNull(message = "User id is required") Integer userId,
                                      @NotNull(message = "Schedule id is required") Integer scheduleId,
                                      @NotNull(message = "Availability is required") boolean availability,
                                      String comment) {
}
