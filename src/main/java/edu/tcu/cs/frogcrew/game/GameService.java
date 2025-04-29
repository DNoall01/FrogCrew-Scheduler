package edu.tcu.cs.frogcrew.game;

import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game findById(int gameId) {
        return this.gameRepository.findById(gameId)
                .orElseThrow(() -> new ObjectNotFoundException("game", gameId));
    }

    public List<Game> findAll() {
        return this.gameRepository.findAll();
    }

    public Game update(Integer gameId, Game game) {
        return this.gameRepository.findById(gameId)
                .map(oldGame -> {
                    oldGame.setGameDateTime(game.getGameDateTime());
                    oldGame.setVenue(game.getVenue());
                    oldGame.setOpponent(game.getOpponent());
                    return this.gameRepository.save(oldGame);
                }).orElseThrow(() -> new ObjectNotFoundException("game", gameId));
    }

    public List<Game> findGamesByScheduleId(int scheduleId) {
        return this.gameRepository.findByScheduleId(scheduleId);
    }

}
