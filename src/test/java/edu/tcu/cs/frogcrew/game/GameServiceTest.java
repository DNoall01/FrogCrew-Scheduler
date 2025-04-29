package edu.tcu.cs.frogcrew.game;

import edu.tcu.cs.frogcrew.schedule.Schedule;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    GameRepository gameRepository;

    @InjectMocks
    GameService gameService;

    private Schedule s1;
    private Game g1;
    private Game g2;

    @BeforeEach
    void setUp() {

        s1 = new Schedule();
        s1.setId(1);
        s1.setSport("Football");
        s1.setSeason("Fall 2025");

        // Create mock game 1
        g1 = new Game();
        g1.setGameId(101);
        g1.setSchedule(s1);
        g1.setSport("Football");
        g1.setGameDateTime(LocalDateTime.of(LocalDate.of(2025, 10, 10), LocalTime.of(12, 0)));
        g1.setVenue("Amon G. Carter Stadium");
        g1.setOpponent("Texas Longhorns");
        g1.setFinalized(true);
        g1.setPositions(List.of("Usher", "Gate Keeper"));

        // Create mock game 2
        g2 = new Game();
        g2.setGameId(102);
        g2.setSchedule(s1);
        g2.setSport("Football");
        g2.setGameDateTime(LocalDateTime.of(LocalDate.of(2025, 10, 17), LocalTime.of(12, 0)));
        g2.setVenue("Amon G. Carter Stadium");
        g2.setOpponent("Oklahoma Sooners");
        g2.setFinalized(false);
        g2.setPositions(List.of("Ticket Checker"));

        // Add games to schedule
        s1.setGames(List.of(g1, g2));
    }

    @Test
    void testFindByIdSuccess() {
        given(gameRepository.findById(101)).willReturn(Optional.of(g1));

        Game result = this.gameService.findById(101);

        assertThat(result.getGameId()).isEqualTo(101);
        assertThat(result.getSport()).isEqualTo("Football");
        assertThat(result.getVenue()).isEqualTo("Amon G. Carter Stadium");
        assertThat(result.getOpponent()).isEqualTo("Texas Longhorns");
        assertThat(result.getSchedule()).isEqualTo(s1);
        verify(gameRepository, times(1)).findById(101);
    }

    @Test
    void testFindByIdNotFound() {
        given(gameRepository.findById(any(Integer.class))).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> {
            Game returnedGame = this.gameService.findById(101);
        });

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find game with id 101");
        verify(gameRepository, times(1)).findById(101);
    }

    @Test
    void testFindAllSuccess() {
        given(gameRepository.findAll()).willReturn(List.of(g1, g2));

        List<Game> result = this.gameService.findAll();

        assertThat(result).hasSize(2);
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    void testUpdateSuccess() {
        Game update = new Game();
        update.setGameDateTime(LocalDateTime.of(LocalDate.of(2025, 11, 15), LocalTime.of(12, 0)));
        update.setSport("Football");
        update.setVenue("Amon G. Carter Stadium");
        update.setOpponent("Cleveland Browns");
        update.setFinalized(false);
        update.setSchedule(s1);

        given(gameRepository.findById(101)).willReturn(Optional.of(g1));
        given(gameRepository.save(any(Game.class))).willAnswer(inv -> inv.getArgument(0));
        Game result = this.gameService.update(101, update);

        assertThat(result.getGameId()).isEqualTo(101);
        assertThat(result.getSport()).isEqualTo("Football");
        assertThat(result.getVenue()).isEqualTo("Amon G. Carter Stadium");
        assertThat(result.getOpponent()).isEqualTo("Cleveland Browns");
        assertThat(result.getSchedule()).isEqualTo(s1);
        verify(gameRepository, times(1)).findById(101);
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void testFindGamesByScheduleIdSuccess() {
        given(gameRepository.findByScheduleId(1)).willReturn(List.of(g1, g2));

        List<Game> result = this.gameService.findGamesByScheduleId(1);

        assertThat(result).hasSize(2);
        verify(gameRepository, times(1)).findByScheduleId(1);
    }

}
