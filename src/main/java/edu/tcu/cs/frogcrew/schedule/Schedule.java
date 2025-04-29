package edu.tcu.cs.frogcrew.schedule;

import edu.tcu.cs.frogcrew.game.Game;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String sport;

    private String season;

    @OneToMany
    private List<Game> games = new ArrayList<>();

    public void createSchedule() {}

    public void viewSchedule() {}

    public void publishSchedule() {}


    // GETTERS AND SETTERS


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
