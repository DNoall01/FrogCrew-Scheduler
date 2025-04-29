package edu.tcu.cs.frogcrew.schedule;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.GameRepository;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GameRepository     gameRepository;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           GameRepository gameRepository) {
        this.scheduleRepository = scheduleRepository;
        this.gameRepository     = gameRepository;
    }

    /* ─── schedules ─── */

    public Schedule findById(Integer id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("schedule", id));
    }

    public List<Schedule> findBySeason(String season) {
        return scheduleRepository.findSchedulesBySeason(season);
    }

    public Schedule save(Schedule s)             { return scheduleRepository.save(s); }

    public Schedule update(Integer id, Schedule u) {
        return scheduleRepository.findById(id)
                .map(old -> {
                    old.setSport(u.getSport());
                    old.setSeason(u.getSeason());
                    old.setGames(u.getGames());
                    return scheduleRepository.save(old);
                })
                .orElseThrow(() -> new ObjectNotFoundException("schedule", id));
    }

    public List<String> getAllSports()           { return scheduleRepository.findDistinctSports(); }

    /* ─── games ─── */

    public Game addGameToSchedule(Integer scheduleId, Game g) {
        Schedule sched = findById(scheduleId);
        g.setSchedule(sched);
        sched.getGames().add(g);
        return gameRepository.save(g);
    }

    /** Remove game and save parent; orphan-removal will delete row */
    public void removeGame(Integer scheduleId, Integer gameId) {
        Schedule sched = findById(scheduleId);

        boolean removed = sched.getGames()
                               .removeIf(g -> g.getGameId() == gameId); // <- primitive comparison

        if (!removed) throw new ObjectNotFoundException("game", gameId);

        scheduleRepository.save(sched);
    }

    public List<Game> findAllGames()             { return gameRepository.findAll(); }
}
