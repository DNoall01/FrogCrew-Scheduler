package edu.tcu.cs.frogcrew.game;

import edu.tcu.cs.frogcrew.game.converter.GameDtoToGameConverter;
import edu.tcu.cs.frogcrew.game.converter.GameToGameDtoConverter;
import edu.tcu.cs.frogcrew.game.dto.GameDto;
import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/gameSchedule")
public class GameController {

    private final GameService gameService;
    private final GameToGameDtoConverter gameToGameDtoConverter;
    private final GameDtoToGameConverter gameDtoToGameConverter;

    public GameController(GameService gameService, GameToGameDtoConverter gameToGameDtoConverter, GameDtoToGameConverter gameDtoToGameConverter) {
        this.gameService = gameService;
        this.gameToGameDtoConverter = gameToGameDtoConverter;
        this.gameDtoToGameConverter = gameDtoToGameConverter;
    }

    @GetMapping("/games")
    public Result findAllGames() {
        List<Game> foundGames = this.gameService.findAll();
        List<GameDto> gameDtos = foundGames.stream()
                .map(gameToGameDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find Success", gameDtos);
    }

    @GetMapping("/game/{gameId}")
    public Result findGameById(@PathVariable Integer gameId) {
        Game foundGame = this.gameService.findById(gameId);
        GameDto gameDto = this.gameToGameDtoConverter.convert(foundGame);
        return new Result(true, StatusCode.SUCCESS, "Find Success", gameDto);
    }

    @PutMapping("/game/{gameId}")
    public Result updateGame(@PathVariable Integer gameId,@Valid @RequestBody GameDto gameDto) {
        Game update = this.gameDtoToGameConverter.convert(gameDto);
        Game updatedGame = this.gameService.update(gameId, update);
        GameDto updatedGameDto = this.gameToGameDtoConverter.convert(updatedGame);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedGameDto);
    }

    @GetMapping("/{scheduleId}/games")
    public Result findGamesByScheduleId(@PathVariable int scheduleId) {
        List<Game> foundGames = this.gameService.findGamesByScheduleId(scheduleId);
        List<GameDto> gameDtos = foundGames.stream()
                .map(gameToGameDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find Success", gameDtos);
    }


}
