package edu.tcu.cs.frogcrew.crewschedule.dto;

import edu.tcu.cs.frogcrew.creweduser.CrewedUser;
import edu.tcu.cs.frogcrew.creweduser.dto.CrewedMemberDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CrewScheduleDto(
        Integer gameId,
        LocalTime gameStart,
        LocalDate gameDate,
        String venue,
        String opponent,
        List<CrewedMemberDto> crewedMembers
) {
}
