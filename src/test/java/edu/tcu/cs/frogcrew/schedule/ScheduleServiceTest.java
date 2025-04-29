package edu.tcu.cs.frogcrew.schedule;

import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.GameRepository;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    GameRepository gameRepository;

    @InjectMocks
    ScheduleService scheduleService;

    Schedule s1;
    Schedule s2;
    List<Schedule> schedules;

    @BeforeEach
    void setUp() {
        schedules = new ArrayList<>();

        s1 = new Schedule();
        s1.setId(1);
        s1.setSport("Basketball");
        s1.setSeason("Spring");

        s2 = new Schedule();
        s2.setId(2);
        s2.setSport("Football");
        s2.setSeason("Spring");

        schedules.add(s1);
        schedules.add(s2);

    }

    @Test
    void testFindByIdSuccess() {
        given(this.scheduleRepository.findById(1)).willReturn(Optional.of(s1));

        Schedule foundSchedule = this.scheduleService.findById(1);

        assertThat(foundSchedule.getId()).isEqualTo(1);
        assertThat(foundSchedule.getSport()).isEqualTo("Basketball");
        assertThat(foundSchedule.getSeason()).isEqualTo("Spring");
        verify(this.scheduleRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        given(this.scheduleRepository.findById(1)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> this.scheduleService.findById(1));

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
        verify(scheduleRepository, times(1)).findById(1);
    }

    @Test
    void testFindBySeasonSuccess() {
        given(this.scheduleRepository.findSchedulesBySeason("Spring")).willReturn(schedules);

        List<Schedule> foundSchedules = this.scheduleService.findBySeason("Spring");

        assertThat(foundSchedules.size()).isEqualTo(2);
        assertThat(foundSchedules.get(0).getSport()).isEqualTo("Basketball");
        assertThat(foundSchedules.get(1).getSport()).isEqualTo("Football");
        verify(this.scheduleRepository, times(1)).findSchedulesBySeason("Spring");
    }

    @Test
    void testAddGameToScheduleSuccess() {
        Schedule schedule = new Schedule();
        schedule.setId(1);
        schedule.setSport("Soccer");
        schedule.setSeason("Fall 2025");

        Game game = new Game();
        game.setGameId(10);
        game.setOpponent("Rivals");

        given(this.scheduleRepository.findById(1)).willReturn(Optional.of(schedule));
        given(this.gameRepository.save(Mockito.any(Game.class))).willAnswer(inv -> inv.getArgument(0));


        Game savedGame = this.scheduleService.addGameToSchedule(1, game);

        assertThat(savedGame.getGameId()).isEqualTo(10);
        assertThat(savedGame.getOpponent()).isEqualTo("Rivals");
        assertThat(savedGame.getSchedule()).isEqualTo(schedule);
        assertThat(schedule.getGames()).contains(game);
        verify(this.scheduleRepository, times(1)).findById(1);
    }

    @Test
    void testAddGameToScheduleNotFound() {
        Game game = new Game();
        game.setGameId(10);
        game.setOpponent("Rivals");

        given(this.scheduleRepository.findById(99)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> this.scheduleService.addGameToSchedule(99, game));

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
        verify(this.scheduleRepository, times(1)).findById(99);
    }

    @Test
    void testSaveSuccess() {
        Schedule newSchedule = new Schedule();
        newSchedule.setSport("Basketball");
        newSchedule.setSeason("Spring 2026");

        given(this.scheduleRepository.save(newSchedule)).willReturn(newSchedule);

        Schedule savedSchedule = this.scheduleService.save(newSchedule);

        assertThat(savedSchedule.getSport()).isEqualTo("Basketball");
        assertThat(savedSchedule.getSeason()).isEqualTo("Spring 2026");
        verify(this.scheduleRepository, times(1)).save(newSchedule);
    }

    @Test
    void testUpdateSuccess() {

        Schedule existingSchedule = new Schedule();
        existingSchedule.setId(1);
        existingSchedule.setSport("Basketball");
        existingSchedule.setSeason("Spring 2026");

        Schedule updatedSchedule = new Schedule();
        updatedSchedule.setSport("Soccer");
        updatedSchedule.setSeason("Fall 2026");

        given(this.scheduleRepository.findById(1)).willReturn(Optional.of(existingSchedule));
        given(this.scheduleRepository.save(existingSchedule)).willReturn(existingSchedule);

        Schedule result = this.scheduleService.update(1, updatedSchedule);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getSport()).isEqualTo("Soccer");
        assertThat(result.getSeason()).isEqualTo("Fall 2026");
        verify(this.scheduleRepository, times(1)).findById(1);
        verify(this.scheduleRepository, times(1)).save(existingSchedule);

    }

    @Test
    void testUpdateNotFound() {
        Integer nonExistentId = 99;
        Schedule updatedSchedule = new Schedule();
        updatedSchedule.setSport("Swimming");
        updatedSchedule.setSeason("Fall 2026");

        given(this.scheduleRepository.findById(nonExistentId)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> this.scheduleService.update(nonExistentId, updatedSchedule));

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
        verify(this.scheduleRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testGetAllSports() {
        List<String> sportsList = List.of("Football", "Basketball", "Soccer");
        given(this.scheduleRepository.findDistinctSports()).willReturn(sportsList);

        List<String> result = this.scheduleService.getAllSports();

        assertThat(result.size()).isEqualTo(sportsList.size());
        verify(this.scheduleRepository, times(1)).findDistinctSports();
    }


}
