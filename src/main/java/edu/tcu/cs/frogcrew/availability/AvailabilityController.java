package edu.tcu.cs.frogcrew.availability;

import edu.tcu.cs.frogcrew.availability.converter.AvailabilityGameDtoToAvailabilityConverter;
import edu.tcu.cs.frogcrew.availability.converter.AvailabilityToAvailabilityGameDtoConverter;
import edu.tcu.cs.frogcrew.availability.converter.AvailabilityToAvailabilityScheduleDtoConverter;
import edu.tcu.cs.frogcrew.availability.dto.AvailabilityGameDto;
import edu.tcu.cs.frogcrew.availability.dto.AvailabilityScheduleDto;
import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;
    private final AvailabilityGameDtoToAvailabilityConverter availabilityGameDtoToAvailabilityConverter;
    private final AvailabilityToAvailabilityGameDtoConverter availabilityToAvailabilityGameDtoConverter;
    private final AvailabilityToAvailabilityScheduleDtoConverter availabilityToAvailabilityScheduleDtoConverter;

    public AvailabilityController(AvailabilityService availabilityService, AvailabilityGameDtoToAvailabilityConverter availabilityGameDtoToAvailabilityConverter, AvailabilityToAvailabilityGameDtoConverter availabilityToAvailabilityGameDtoConverter, AvailabilityToAvailabilityScheduleDtoConverter availabilityToAvailabilityScheduleDtoConverter) {
        this.availabilityService = availabilityService;
        this.availabilityGameDtoToAvailabilityConverter = availabilityGameDtoToAvailabilityConverter;
        this.availabilityToAvailabilityGameDtoConverter = availabilityToAvailabilityGameDtoConverter;
        this.availabilityToAvailabilityScheduleDtoConverter = availabilityToAvailabilityScheduleDtoConverter;
    }

    @PostMapping("")
    public Result addAvailability(@Valid @RequestBody AvailabilityGameDto availabilityDto) {
        Availability newAvailability = this.availabilityGameDtoToAvailabilityConverter.convert(availabilityDto);
        Availability savedAvailability = this.availabilityService.save(newAvailability);
        AvailabilityGameDto savedAvailabilityDto = this.availabilityToAvailabilityGameDtoConverter.convert(savedAvailability);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedAvailabilityDto);
    }

    @PutMapping("")
    public Result updateAvailability(@Valid @RequestBody AvailabilityGameDto availabilityDto) {
        AvailabilityId availabilityId = new AvailabilityId(availabilityDto.userId(), availabilityDto.gameId());
        Availability update = this.availabilityGameDtoToAvailabilityConverter.convert(availabilityDto);
        Availability updatedAvailability = this.availabilityService.update(update, availabilityId);
        AvailabilityGameDto savedAvailabilityDto = this.availabilityToAvailabilityGameDtoConverter.convert(updatedAvailability);
        return new Result(true, StatusCode.SUCCESS, "Update Success", savedAvailabilityDto);
    }

    @GetMapping("/{userId}/schedule/{scheduleId}")
    public Result findAvailabilityByUserIdAndScheduleId(@PathVariable Integer userId, @PathVariable Integer scheduleId) {
        List<Availability> foundAvailabilities = this.availabilityService.findByUserIdAndScheduleId(userId, scheduleId);
        List<AvailabilityScheduleDto> availabilityDtos = foundAvailabilities.stream()
                .map(this.availabilityToAvailabilityScheduleDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find Success", availabilityDtos);
    }

    // TODO: Figure out solution to season recognition
    @GetMapping("/{userId}/season/{season}")
    public Result findAvailabilityByUserIdAndSeason(@PathVariable Integer userId, @PathVariable String season) {
        List<Availability> foundAvailability = this.availabilityService.findByUserIdAndSeason(userId, season);
        List<AvailabilityScheduleDto> availabilityDtos = foundAvailability.stream()
                .map(this.availabilityToAvailabilityScheduleDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find Success", availabilityDtos);
    }

}
