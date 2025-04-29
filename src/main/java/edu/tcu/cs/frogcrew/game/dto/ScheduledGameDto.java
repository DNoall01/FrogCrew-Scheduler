package edu.tcu.cs.frogcrew.game.dto;

import java.time.LocalDate;

public record ScheduledGameDto(
        Integer gameId,
        LocalDate gameDate,
        String venue,
        String opponent,
        boolean isFinalized
) {
}
