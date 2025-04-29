package edu.tcu.cs.frogcrew.crewlist.dto;

import edu.tcu.cs.frogcrew.creweduser.CrewedUser;
import edu.tcu.cs.frogcrew.creweduser.dto.CrewedMemberDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CrewListDto(
        Integer gameId,
        LocalTime gameStart,
        LocalDate gameDate,
        String venue,
        String opponent,
        List<CrewedMemberDto> crewedMembers
) {
}
