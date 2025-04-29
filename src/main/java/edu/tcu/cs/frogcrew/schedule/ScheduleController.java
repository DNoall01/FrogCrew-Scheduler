package edu.tcu.cs.frogcrew.schedule;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.converter.GameDtoToGameConverter;
import edu.tcu.cs.frogcrew.game.converter.GameToGameDtoConverter;
import edu.tcu.cs.frogcrew.game.dto.GameDto;
import edu.tcu.cs.frogcrew.schedule.converter.ScheduleDtoToScheduleConverter;
import edu.tcu.cs.frogcrew.schedule.converter.ScheduleToScheduleDtoConverter;
import edu.tcu.cs.frogcrew.schedule.dto.ScheduleDto;
import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/gameSchedule")
public class ScheduleController {

    /* ── ctor-injected deps ── */
    private final ScheduleService                scheduleService;
    private final ScheduleToScheduleDtoConverter toDto;
    private final ScheduleDtoToScheduleConverter fromDto;
    private final GameDtoToGameConverter         gameFromDto;
    private final GameToGameDtoConverter         gameToDto;

    public ScheduleController(
            ScheduleService scheduleService,
            ScheduleToScheduleDtoConverter toDto,
            ScheduleDtoToScheduleConverter fromDto,
            GameDtoToGameConverter gameFromDto,
            GameToGameDtoConverter gameToDto) {
        this.scheduleService = scheduleService;
        this.toDto           = toDto;
        this.fromDto         = fromDto;
        this.gameFromDto     = gameFromDto;
        this.gameToDto       = gameToDto;
    }

    /* ───────── schedules ───────── */

    @PostMapping
    public Result addSchedule(@Valid @RequestBody ScheduleDto dto) {
        var saved = scheduleService.save(fromDto.convert(dto));
        return new Result(true, StatusCode.SUCCESS, "Add Success", toDto.convert(saved));
    }

    @GetMapping("/{id}")
    public Result getSchedule(@PathVariable Integer id) {
        var found = scheduleService.findById(id);
        return new Result(true, StatusCode.SUCCESS, "Find Success", toDto.convert(found));
    }

    @PutMapping("/{id}")
    public Result updateSchedule(@PathVariable Integer id,
                                 @Valid @RequestBody ScheduleDto dto) {
        var updated = scheduleService.update(id, fromDto.convert(dto));
        return new Result(true, StatusCode.SUCCESS, "Update Success", toDto.convert(updated));
    }

    @GetMapping("/season/{season}")
    public Result findSchedulesBySeason(@PathVariable String season) {
        List<ScheduleDto> dtos = scheduleService.findBySeason(season).stream()
                .map(toDto::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find Success", dtos);
    }

    /* ───────── games inside a schedule ───────── */

    @PostMapping("/{scheduleId}/games")
    public Result addGameToSchedule(@PathVariable Integer scheduleId,
                                    @Valid @RequestBody GameDto gameDto) {

        Game saved = scheduleService.addGameToSchedule(
                scheduleId,
                gameFromDto.convert(gameDto)
        );
        return new Result(true, StatusCode.SUCCESS, "Add Success", gameToDto.convert(saved));
    }

    @DeleteMapping("/{scheduleId}/games/{gameId}")
    public Result deleteGameFromSchedule(@PathVariable Integer scheduleId,
                                         @PathVariable Integer gameId) {
        scheduleService.removeGame(scheduleId, gameId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success", null);
    }

    /* ───────── misc helpers ───────── */

    @PutMapping("/publish/{scheduleId}")
    public Result publish(@PathVariable Integer scheduleId, @Valid @RequestBody ScheduleDto dto) {
        return new Result(false, StatusCode.SUCCESS, "Publish not implemented", null);
    }

    @GetMapping("/sports")
    public Result findAllSports() {
        var sports = scheduleService.getAllSports();
        return new Result(true, StatusCode.SUCCESS, "Find Success", sports);
    }
}
