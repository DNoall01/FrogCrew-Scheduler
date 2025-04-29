package edu.tcu.cs.frogcrew.schedule.dto;

import jakarta.validation.constraints.NotEmpty;

public record ScheduleDto(Integer id,
                          @NotEmpty(message = "Sport is required") String sport,
                          @NotEmpty(message = "Season is required") String season) {
}
