package edu.tcu.cs.frogcrew.tradeboard;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.position.Position;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import jakarta.persistence.*;

@Entity
public class TradeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer tradeId;

    @ManyToOne
    @JoinColumn(name = "dropper_id", nullable = false)
    private FrogCrewUser dropper;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private FrogCrewUser receiver;

    @ManyToOne
    @JoinColumn(name = "gameId", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "position", nullable = false)
    private Position position;

    private String status;

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public FrogCrewUser getDropper() {
        return dropper;
    }

    public void setDropper(FrogCrewUser dropper) {
        this.dropper = dropper;
    }

    public FrogCrewUser getReceiver() {
        return receiver;
    }

    public void setReceiver(FrogCrewUser receiver) {
        this.receiver = receiver;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
