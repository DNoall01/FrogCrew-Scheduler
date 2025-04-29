package edu.tcu.cs.frogcrew.tradeboard.converter;

import edu.tcu.cs.frogcrew.tradeboard.TradeBoard;
import edu.tcu.cs.frogcrew.tradeboard.dto.TradeBoardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TradeBoardToTradeBoardDtoConverter implements Converter<TradeBoard, TradeBoardDto> {

    @Override
    public TradeBoardDto convert(TradeBoard source) {
        return new TradeBoardDto(source.getTradeId(), source.getDropper().getId(), source.getGame().getGameId(), source.getPosition().getPositionName(), source.getStatus(), source.getReceiver().getId());
    }
}
