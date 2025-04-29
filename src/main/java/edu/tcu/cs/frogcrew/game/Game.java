package edu.tcu.cs.frogcrew.game;

import edu.tcu.cs.frogcrew.creweduser.CrewedUser;
import edu.tcu.cs.frogcrew.schedule.Schedule;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int gameId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private String sport;

    private LocalDateTime gameDateTime;

    private String venue;

    private String opponent;

    private boolean finalized = false;

    @ElementCollection
    private List<String> positions;


    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<CrewedUser> crewMembers;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public LocalDateTime getGameDateTime() {
        return gameDateTime;
    }

    public void setGameDateTime(LocalDateTime gameDate) {
        this.gameDateTime = gameDate;
    }

    public LocalDate getGameDate() {
        return gameDateTime.toLocalDate();
    }

    public LocalTime getGameTime() {
        return gameDateTime.toLocalTime();
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public List<String> getPositions() {
        return positions;
    }

    public void setPositions(List<String> openPositions) {
        this.positions = openPositions;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

    public List<CrewedUser> getCrewMembers() {
        return crewMembers;
    }

    public void setCrewMembers(List<CrewedUser> crewMembers) {
        this.crewMembers = crewMembers;
    }
}
