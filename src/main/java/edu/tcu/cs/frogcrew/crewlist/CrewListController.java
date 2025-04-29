package edu.tcu.cs.frogcrew.crewlist;

import edu.tcu.cs.frogcrew.creweduser.converter.CrewedUserToCrewMemberDtoConverter;
import edu.tcu.cs.frogcrew.crewlist.converter.GameToCrewListDtoConverter;
import edu.tcu.cs.frogcrew.crewlist.dto.CrewListDto;
import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/crewList")
public class CrewListController {

    private final CrewListService crewListService;
    private final GameToCrewListDtoConverter gameToCrewListDtoConverter;
    private final CrewedUserToCrewMemberDtoConverter crewedUserToCrewMemberDtoConverter;

    public CrewListController(CrewListService crewListService, GameToCrewListDtoConverter gameToCrewListDtoConverter, CrewedUserToCrewMemberDtoConverter crewedUserToCrewMemberDtoConverter) {
        this.crewListService = crewListService;
        this.gameToCrewListDtoConverter = gameToCrewListDtoConverter;
        this.crewedUserToCrewMemberDtoConverter = crewedUserToCrewMemberDtoConverter;
    }

    @GetMapping("/{gameId}")
    public Result findCrewListByGameId(@PathVariable Integer gameId) {
        CrewListDto crewListDto = this.crewListService.findCrewListByGameId(gameId);
        return new Result(true, StatusCode.SUCCESS, "Find Success", crewListDto);
    }
}
