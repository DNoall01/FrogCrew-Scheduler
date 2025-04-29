package edu.tcu.cs.frogcrew.position;

import edu.tcu.cs.frogcrew.position.converter.PositionDtoToPositionConverter;
import edu.tcu.cs.frogcrew.position.converter.PositionToPositionDtoConverter;
import edu.tcu.cs.frogcrew.position.dto.PositionDto;
import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/positions")
public class PositionController {

    private final PositionService positionService;
    private final PositionToPositionDtoConverter positionToPositionDtoConverter;
    private final PositionDtoToPositionConverter positionDtoToPositionConverter;

    public PositionController(PositionService positionService, PositionToPositionDtoConverter positionToPositionDtoConverter, PositionDtoToPositionConverter positionDtoToPositionConverter) {
        this.positionService = positionService;
        this.positionToPositionDtoConverter = positionToPositionDtoConverter;
        this.positionDtoToPositionConverter = positionDtoToPositionConverter;
    }

    @GetMapping
    public Result findAllPositions() {
        List<String> positions = this.positionService.findAllPositionNames();
        return new Result(true, StatusCode.SUCCESS, "Find Success", positions);
    }

    @PostMapping
    public Result addPosition(@RequestBody PositionDto positionDto) {
        Position newPosition = this.positionDtoToPositionConverter.convert(positionDto);
        Position savedPosition = this.positionService.save(newPosition);
        PositionDto savedPositionDto = this.positionToPositionDtoConverter.convert(savedPosition);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedPositionDto);
    }

    @PutMapping("/{positionId}")
    public Result updatePosition(@PathVariable("positionId") Integer positionId, @Valid @RequestBody PositionDto positionDto) {
        Position update = this.positionDtoToPositionConverter.convert(positionDto);
        Position updatedPosition = this.positionService.update(positionId, update);
        PositionDto updatedPositionDto = this.positionToPositionDtoConverter.convert(updatedPosition);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedPositionDto);
    }
}
