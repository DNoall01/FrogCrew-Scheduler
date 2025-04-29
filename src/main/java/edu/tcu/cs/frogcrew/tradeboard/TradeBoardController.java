package edu.tcu.cs.frogcrew.tradeboard;

import edu.tcu.cs.frogcrew.game.dto.ScheduledGameDto;
import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import edu.tcu.cs.frogcrew.tradeboard.converter.TradeBoardDtoToTradeBoardConverter;
import edu.tcu.cs.frogcrew.tradeboard.converter.TradeBoardToTradeBoardDtoConverter;
import edu.tcu.cs.frogcrew.tradeboard.dto.TradeBoardDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/scheduledGames")
public class TradeBoardController {

    private final TradeBoardService tradeBoardService;
    private final TradeBoardToTradeBoardDtoConverter tradeBoardToTradeBoardDtoConverter;
    private final TradeBoardDtoToTradeBoardConverter tradeBoardDtoToTradeBoardConverter;

    public TradeBoardController(TradeBoardService tradeBoardService, TradeBoardToTradeBoardDtoConverter tradeBoardToTradeBoardDtoConverter, TradeBoardDtoToTradeBoardConverter tradeBoardDtoToTradeBoardConverter) {
        this.tradeBoardService = tradeBoardService;
        this.tradeBoardToTradeBoardDtoConverter = tradeBoardToTradeBoardDtoConverter;
        this.tradeBoardDtoToTradeBoardConverter = tradeBoardDtoToTradeBoardConverter;
    }

    @PostMapping("/drop")
    public Result addShiftToTradeBoard(@RequestBody final TradeBoardDto tradeBoardDto) {
        TradeBoard tradeBoard = tradeBoardDtoToTradeBoardConverter.convert(tradeBoardDto);
        TradeBoard savedTradeboard = this.tradeBoardService.saveTradeBoard(tradeBoard);
        TradeBoardDto savedDto = this.tradeBoardToTradeBoardDtoConverter.convert(savedTradeboard);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedDto);
    }

    @PutMapping("/pickup/{tradeId}/{userId}")
    public Result pickUpTradeBoard(@PathVariable final Integer tradeId, @PathVariable final Integer userId) {
        TradeBoard tradeBoard = this.tradeBoardService.requestPickupGame(tradeId, userId);
        TradeBoardDto tradeBoardDto = this.tradeBoardToTradeBoardDtoConverter.convert(tradeBoard);
        return new Result(true, StatusCode.SUCCESS, "Request Success", tradeBoardDto);
    }

    @PutMapping("/approve/{tradeId}")
    public Result approveShiftSwap(@PathVariable final Integer tradeId) {
        TradeBoard tradeBoard = this.tradeBoardService.approveShiftSwap(tradeId);
        TradeBoardDto tradeBoardDto = this.tradeBoardToTradeBoardDtoConverter.convert(tradeBoard);
        return new Result(true, StatusCode.SUCCESS, "Approval Success", tradeBoardDto);
    }

    @PutMapping("/deny/{tradeId}")
    public Result denyShiftSwap(@PathVariable final Integer tradeId) {
        TradeBoard tradeBoard = this.tradeBoardService.rejectShiftSwap(tradeId);
        TradeBoardDto tradeBoardDto = this.tradeBoardToTradeBoardDtoConverter.convert(tradeBoard);
        return new Result(true, StatusCode.SUCCESS, "Denial Success", tradeBoardDto);
    }

    @GetMapping("/tradeboard")
    public Result findAllTradeBoards() {
        List<TradeBoard> tradeBoards = this.tradeBoardService.findAll();
        List<TradeBoardDto> dtos = tradeBoards.stream().map(tradeBoardToTradeBoardDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Find Success", dtos);
    }

    @GetMapping("/get/{userId}")
    public Result findGamesByUserId(@PathVariable final Integer userId) {
        List<ScheduledGameDto> games = this.tradeBoardService.findScheduledGamesByUserId(userId);
        return new Result(true, StatusCode.SUCCESS, "Find Success", games);
    }

}
