// src/main/java/edu/tcu/cs/frogcrew/game/dto/GameDto.java
package edu.tcu.cs.frogcrew.game.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

public record GameDto(
    Integer gameId,
    Integer scheduleId,
    @NotNull(message = "Game date is required") LocalDateTime gameDate,
    @NotEmpty(message = "Venue is required") String venue,
    @NotEmpty(message = "Opponent is required") String opponent,
    boolean finalized,
    @NotNull(message = "Required positions are required") List<String> requiredPositions
) {
    /**
     * Convenience constructor so older tests (and any other code) that
     * call the 6-arg version continue to compile.
     */
    public GameDto(
        Integer gameId,
        Integer scheduleId,
        LocalDateTime gameDate,
        String venue,
        String opponent,
        boolean finalized
    ) {
        this(
            gameId,
            scheduleId,
            gameDate,
            venue,
            opponent,
            finalized,
            Collections.emptyList()
        );
    }
}
