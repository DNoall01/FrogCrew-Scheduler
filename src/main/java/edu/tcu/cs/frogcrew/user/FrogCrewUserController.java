package edu.tcu.cs.frogcrew.user;

import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import edu.tcu.cs.frogcrew.user.converter.FrogCrewUserToUserDtoConverter;
import edu.tcu.cs.frogcrew.user.converter.FrogCrewUserToUsersDtoConverter;
import edu.tcu.cs.frogcrew.user.converter.UserDtoToFrogCrewUserConverter;
import edu.tcu.cs.frogcrew.user.dto.UserDto;
import edu.tcu.cs.frogcrew.user.dto.UsersDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/crewMember")
public class FrogCrewUserController {

    private final FrogCrewUserService frogCrewUserService;
    private final FrogCrewUserToUserDtoConverter frogCrewUserToUserDtoConverter;
    private final UserDtoToFrogCrewUserConverter userDtoToFrogCrewUserConverter;
    private final FrogCrewUserToUsersDtoConverter frogCrewUserToUsersDtoConverter;

    public FrogCrewUserController(FrogCrewUserService frogCrewUserService,
                                  FrogCrewUserToUserDtoConverter frogCrewUserToUserDtoConverter,
                                  UserDtoToFrogCrewUserConverter userDtoToFrogCrewUserConverter,
                                  FrogCrewUserToUsersDtoConverter frogCrewUserToUsersDtoConverter) {
        this.frogCrewUserService = frogCrewUserService;
        this.frogCrewUserToUserDtoConverter = frogCrewUserToUserDtoConverter;
        this.userDtoToFrogCrewUserConverter = userDtoToFrogCrewUserConverter;
        this.frogCrewUserToUsersDtoConverter = frogCrewUserToUsersDtoConverter;
    }

    @PostMapping
    public Result addCrewMember(@Valid @RequestBody FrogCrewUser frogCrewUser) {
        FrogCrewUser savedUser = this.frogCrewUserService.addCrewMember(frogCrewUser);
        UserDto dto = this.frogCrewUserToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", dto);
    }

    

    @GetMapping
public Result findCrewMembers() {
  List<FrogCrewUser> found = frogCrewUserService.findAll();
  List<UserDto> dtos = found.stream()
      .map(frogCrewUserToUserDtoConverter::convert)
      .toList();
  return new Result(true, StatusCode.SUCCESS, "Find Success", dtos);
}

    @GetMapping("/{userId}")
    public Result findCrewMemberById(@PathVariable Integer userId) {
        FrogCrewUser foundUser = this.frogCrewUserService.findById(userId);
        UserDto dto = this.frogCrewUserToUserDtoConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS, "Find Success", dto);
    }
    @GetMapping(params = "position")
public Result findByPosition(@RequestParam String position) {

    List<FrogCrewUser> found = frogCrewUserService.findByPosition(position);
    List<UserDto> dtos      = found.stream()
                                   .map(frogCrewUserToUserDtoConverter::convert)
                                   .toList();

    return new Result(true, StatusCode.SUCCESS, "Find Success", dtos);
}

    @PutMapping("/{userId}")
    public Result updateCrewMember(@PathVariable Integer userId,
                                   @Valid @RequestBody UserDto userDto) {
        FrogCrewUser update = this.userDtoToFrogCrewUserConverter.convert(userDto);
        FrogCrewUser updatedFrogCrewUser = this.frogCrewUserService.updateCrewMember(userId, update);
        UserDto updatedDto = this.frogCrewUserToUserDtoConverter.convert(updatedFrogCrewUser);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedDto);
    }

    /** New endpoint to fetch the currently authenticated user’s details (including role) */
    @GetMapping("/me")
    public Result whoAmI(Authentication auth) {
        FrogCrewUser me = frogCrewUserService.findByUsername(auth.getName());
        UserDto dto = this.frogCrewUserToUserDtoConverter.convert(me);
        return new Result(true, StatusCode.SUCCESS, "Current user", dto);
    }

@DeleteMapping("/{userId}")
public Result deleteCrewMember(@PathVariable Integer userId) {
    frogCrewUserService.deleteCrewMember(userId);
    return new Result(true, StatusCode.SUCCESS, "Delete Success");
}

}
