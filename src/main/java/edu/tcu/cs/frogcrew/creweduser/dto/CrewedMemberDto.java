package edu.tcu.cs.frogcrew.creweduser.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record CrewedMemberDto(
        Integer crewMemberId,
        @NotNull(message = "UserId is required") Integer userId,
        Integer gameId,
        String fullName,
        @NotNull(message = "Position is required") String position,
        LocalTime reportTime,
        String reportLocation
) {
}
