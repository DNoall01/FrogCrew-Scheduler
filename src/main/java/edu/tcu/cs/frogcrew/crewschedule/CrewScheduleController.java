package edu.tcu.cs.frogcrew.crewschedule;

import edu.tcu.cs.frogcrew.creweduser.CrewedUser;
import edu.tcu.cs.frogcrew.creweduser.converter.CrewedUserToCrewMemberDtoConverter;
import edu.tcu.cs.frogcrew.creweduser.dto.CrewedMemberDto;
import edu.tcu.cs.frogcrew.crewschedule.converter.GameToCrewScheduleDtoConverter;
import edu.tcu.cs.frogcrew.crewschedule.dto.CrewScheduleDto;
import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/crewSchedule")
public class CrewScheduleController {

    private final CrewScheduleService crewScheduleService;
    private final CrewedUserToCrewMemberDtoConverter crewedUserToCrewMemberDtoConverter;
    private final GameToCrewScheduleDtoConverter gameToCrewScheduleDtoConverter;

    public CrewScheduleController(CrewScheduleService crewScheduleService, CrewedUserToCrewMemberDtoConverter crewedUserToCrewMemberDtoConverter, GameToCrewScheduleDtoConverter gameToCrewScheduleDtoConverter) {
        this.crewScheduleService = crewScheduleService;
        this.crewedUserToCrewMemberDtoConverter = crewedUserToCrewMemberDtoConverter;
        this.gameToCrewScheduleDtoConverter = gameToCrewScheduleDtoConverter;
    }

    @GetMapping("/{gameId}")
    public Result findCrewScheduleById(@PathVariable Integer gameId) {
        Game game = crewScheduleService.findGameById(gameId);
        CrewScheduleDto crewScheduleDto = gameToCrewScheduleDtoConverter.convert(game);
        return new Result(true, StatusCode.SUCCESS, "Find Success", crewScheduleDto);
    }

    @PostMapping("/{gameId}")
    public Result addNewCrewSchedule(@PathVariable Integer gameId,@Valid @RequestBody List<CrewedMemberDto> crewScheduleDto) {
        List<CrewedUser> crewedUsers = this.crewScheduleService.addCrewSchedule(gameId, crewScheduleDto);
        List<CrewedMemberDto> dtos = crewedUsers.stream().map(crewedUserToCrewMemberDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Add Success", dtos);
    }

    @PutMapping
    public Result updateCrewSchedule(@Valid @RequestBody List<CrewedMemberDto> crewScheduleDto) {
        List<CrewedUser> updatedCrewSchedule = this.crewScheduleService.updateCrewSchedule(crewScheduleDto);
        List<CrewedMemberDto> dtos = updatedCrewSchedule.stream().map(crewedUserToCrewMemberDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Update Success", dtos);
    }

    @DeleteMapping("/{gameId}")
    public Result deleteCrewSchedule(@PathVariable Integer gameId) {
        this.crewScheduleService.deleteCrewScheduleByGameId(gameId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
