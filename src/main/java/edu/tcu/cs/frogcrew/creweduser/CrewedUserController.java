package edu.tcu.cs.frogcrew.creweduser;

import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.converter.FrogCrewUserToFrogCrewedUserDtoConverter;
import edu.tcu.cs.frogcrew.user.dto.FrogCrewedUserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/CrewedUser")
public class CrewedUserController {

    private final CrewedUserService crewedUserService;
    private final FrogCrewUserToFrogCrewedUserDtoConverter frogCrewUserToFrogCrewedUserDtoConverter;

    public CrewedUserController(CrewedUserService crewedUserService, FrogCrewUserToFrogCrewedUserDtoConverter frogCrewUserToFrogCrewedUserDtoConverter) {
        this.crewedUserService = crewedUserService;
        this.frogCrewUserToFrogCrewedUserDtoConverter = frogCrewUserToFrogCrewedUserDtoConverter;
    }

    @GetMapping("/{gameId}/{position}")
    public Result findCrewMembersByAvailabilityAndPosition(@PathVariable Integer gameId, @PathVariable String position) {
        List<FrogCrewUser> foundUsers = this.crewedUserService.findCrewedUsersByAvailabilityAndPosition(gameId, position);
        List<FrogCrewedUserDto> dtos = foundUsers.stream()
                .map(frogCrewUserToFrogCrewedUserDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find Success", dtos);
    }
}
