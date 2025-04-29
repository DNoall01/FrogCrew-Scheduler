package edu.tcu.cs.frogcrew.tradeboard.converter;

import edu.tcu.cs.frogcrew.game.GameService;
import edu.tcu.cs.frogcrew.position.PositionService;
import edu.tcu.cs.frogcrew.tradeboard.TradeBoard;
import edu.tcu.cs.frogcrew.tradeboard.TradeBoardService;
import edu.tcu.cs.frogcrew.tradeboard.dto.TradeBoardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TradeBoardDtoToTradeBoardConverter implements Converter<TradeBoardDto, TradeBoard> {

    //private final UserService userService;
    private final GameService gameService;
    private final PositionService positionService;

    public TradeBoardDtoToTradeBoardConverter(GameService gameService, PositionService positionService) {
        this.gameService = gameService;
        this.positionService = positionService;
    }

    @Override
    public TradeBoard convert(TradeBoardDto source) {
        TradeBoard tradeBoard = new TradeBoard();
        tradeBoard.setTradeId(source.tradeId());
        //tradeBoard.setDropper(this.userService.findById(source.dropperId()));
        tradeBoard.setGame(this.gameService.findById(source.gameId()));
        tradeBoard.setPosition(this.positionService.findByPositionName(source.position()));
        tradeBoard.setStatus(source.status());
        //tradeBoard.setReceiver(this.userService.findById(source.receiverId()));
        return tradeBoard;
    }
}
