package edu.tcu.cs.frogcrew.gametypeproperties;

import edu.tcu.cs.frogcrew.gametypeproperties.converter.GameTypePropertiesDtoToGameTypePropertiesConverter;
import edu.tcu.cs.frogcrew.gametypeproperties.converter.GameTypePropertiesPositionNameDtoToGameTypePropertiesConverter;
import edu.tcu.cs.frogcrew.gametypeproperties.converter.GameTypePropertiesToGameTypePropertiesDtoConverter;
import edu.tcu.cs.frogcrew.gametypeproperties.converter.GameTypePropertiesToGameTypePropertiesPositionNameDtoConverter;
import edu.tcu.cs.frogcrew.gametypeproperties.dto.GameTypePropertiesDto;
import edu.tcu.cs.frogcrew.gametypeproperties.dto.GameTypePropertiesPositionNameDto;
import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/positions/properties")
public class GameTypePropertiesController {

    private final GameTypePropertiesService gameTypePropertiesService;
    private final GameTypePropertiesToGameTypePropertiesPositionNameDtoConverter positionNameDtoConverter;
    private final GameTypePropertiesPositionNameDtoToGameTypePropertiesConverter positionNameDtoToGameTypePropertiesConverter;
    private final GameTypePropertiesToGameTypePropertiesDtoConverter gameTypeToDtoConverter;
    private final GameTypePropertiesDtoToGameTypePropertiesConverter gameTypeDtoToGameTypeConverter;


    public GameTypePropertiesController(GameTypePropertiesService gameTypePropertiesService, GameTypePropertiesToGameTypePropertiesPositionNameDtoConverter positionNameDtoConverter, GameTypePropertiesToGameTypePropertiesDtoConverter gameTypeToDtoConverter, GameTypePropertiesDtoToGameTypePropertiesConverter gameTypeDtoToGameTypeConverter, GameTypePropertiesPositionNameDtoToGameTypePropertiesConverter positionNameDtoToGameTypePropertiesConverter) {
        this.gameTypePropertiesService = gameTypePropertiesService;
        this.positionNameDtoConverter = positionNameDtoConverter;
        this.gameTypeToDtoConverter = gameTypeToDtoConverter;
        this.gameTypeDtoToGameTypeConverter = gameTypeDtoToGameTypeConverter;
        this.positionNameDtoToGameTypePropertiesConverter = positionNameDtoToGameTypePropertiesConverter;
    }

    @PostMapping
    public Result addPositionProperties(@Valid @RequestBody final GameTypePropertiesDto gameTypePropertiesDto) {
        GameTypeProperties gameTypeProperties = this.gameTypeDtoToGameTypeConverter.convert(gameTypePropertiesDto);
        GameTypeProperties savedProperties = this.gameTypePropertiesService.addGameTypeProperties(gameTypeProperties);
        GameTypePropertiesDto dto = this.gameTypeToDtoConverter.convert(savedProperties);
        return new Result(true, StatusCode.SUCCESS, "Add Success", dto);
    }

    @PutMapping("/{positionId}/{gameType}")
    public Result editPositionProperties(@PathVariable("positionId") Integer positionId, @PathVariable("gameType") String gameType,@Valid @RequestBody GameTypePropertiesPositionNameDto positionNameDto) {
        GameTypeProperties update = this.positionNameDtoToGameTypePropertiesConverter.convert(positionNameDto);
        GameTypeProperties updatedProperties = this.gameTypePropertiesService.updateGameTypeProperties(positionId, gameType, update);
        GameTypePropertiesPositionNameDto dto = this.positionNameDtoConverter.convert(updatedProperties);
        return new Result(true, StatusCode.SUCCESS, "Update Success", dto);
    }

    @GetMapping("/{gameType}")
    public Result findPropertiesByGameType(@PathVariable("gameType") String gameType) {
        List<GameTypeProperties> foundProperties = this.gameTypePropertiesService.findGameTypePropertiesByGameType(gameType);
        List<GameTypePropertiesPositionNameDto> foundPositionNameDtos = foundProperties.stream()
                .map(positionNameDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find Success", foundPositionNameDtos);
    }

}
