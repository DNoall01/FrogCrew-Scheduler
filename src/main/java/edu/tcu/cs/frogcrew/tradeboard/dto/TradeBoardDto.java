package edu.tcu.cs.frogcrew.tradeboard.dto;

public record TradeBoardDto(Integer tradeId,
                            Integer dropperId,
                            Integer gameId,
                            String position,
                            String status,
                            Integer receiverId) {
}
