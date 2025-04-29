package edu.tcu.cs.frogcrew.position;

import edu.tcu.cs.frogcrew.gametypeproperties.GameTypeProperties;
import jakarta.persistence.*;

@Entity
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer positionId;

    private String positionName;

    private String positionLocation;

    @OneToOne
    GameTypeProperties gameTypeProperties;

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionLocation() {
        return positionLocation;
    }

    public void setPositionLocation(String positionLocation) {
        this.positionLocation = positionLocation;
    }

    public GameTypeProperties getGameTypeProperties() {
        return gameTypeProperties;
    }

    public void setGameTypeProperties(GameTypeProperties gameTypeProperties) {
        this.gameTypeProperties = gameTypeProperties;
    }
}
