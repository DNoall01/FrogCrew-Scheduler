package edu.tcu.cs.frogcrew.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrew.game.dto.GameDto;
import edu.tcu.cs.frogcrew.schedule.Schedule;
import edu.tcu.cs.frogcrew.schedule.ScheduleRepository;
import edu.tcu.cs.frogcrew.system.StatusCode;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GameControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    GameService gameService;

    @MockitoBean
    ScheduleRepository scheduleRepository;

    @Autowired
    ObjectMapper objectMapper;

    List<Game> games;

    @Value("/api/v1")
    String baseUrl;

    private Schedule s1;
    private Schedule s2;

    private Game g1;
    private Game g2;
    private Game g3;
    private Game g4;

    @BeforeEach
    void setUp() {
        s1 = new Schedule();
        s1.setId(1);
        s1.setSport("Football");
        s1.setSeason("Fall 2025");

        s2 = new Schedule();
        s2.setId(2);
        s2.setSport("Basketball");
        s2.setSeason("Winter 2025");

        g1 = new Game();
        g1.setGameId(101);
        g1.setSport("Football");
        g1.setGameDateTime(LocalDateTime.of(LocalDate.of(2025, 9, 21), LocalTime.of(12, 0)));
        g1.setVenue("Amon G. Carter Stadium");
        g1.setOpponent("Oklahoma Sooners");
        g1.setSchedule(s1);
        g1.setFinalized(false);
        g1.setPositions(List.of("Camera", "Sound", "Replay"));

        g2 = new Game();
        g2.setGameId(102);
        g2.setSport("Football");
        g2.setGameDateTime(LocalDateTime.of(LocalDate.of(2025, 10, 5), LocalTime.of(12, 0)));
        g2.setVenue("Amon G. Carter Stadium");
        g2.setOpponent("Baylor Bears");
        g2.setSchedule(s1);
        g2.setFinalized(true);
        g2.setPositions(List.of());

        g3 = new Game();
        g3.setGameId(103);
        g3.setSport("Basketball");
        g3.setGameDateTime(LocalDateTime.of(LocalDate.of(2025, 12, 1), LocalTime.of(12, 0)));
        g3.setVenue("Schollmaier Arena");
        g3.setOpponent("Kansas Jayhawks");
        g3.setSchedule(s2);
        g3.setFinalized(false);
        g3.setPositions(List.of("Camera", "Lighting"));

        g4 = new Game();
        g4.setGameId(104);
        g4.setSport("Basketball");
        g4.setGameDateTime(LocalDateTime.of(LocalDate.of(2025, 12, 15), LocalTime.of(12, 0)));
        g4.setVenue("Schollmaier Arena");
        g4.setOpponent("Texas Tech");
        g4.setSchedule(s2);
        g4.setFinalized(true);
        g4.setPositions(List.of());

        games = new ArrayList<>();
        games.add(g1);
        games.add(g2);
        games.add(g3);
        games.add(g4);

    }

    @Test
    void testFindGameByIdSuccess() throws Exception {
        given(this.gameService.findById(102)).willReturn(this.games.get(1));

        this.mockMvc.perform(get(this.baseUrl + "/gameSchedule/game/102").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data.gameId").value(102))
                .andExpect(jsonPath("$.data.venue").value("Amon G. Carter Stadium"))
                .andExpect(jsonPath("$.data.opponent").value("Baylor Bears"));
    }

    @Test
    void testFindGameByIdNotFound() throws Exception {
        given(this.gameService.findById(110)).willThrow(new ObjectNotFoundException("game", 110));

        this.mockMvc.perform(get(this.baseUrl + "/gameSchedule/game/110").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find game with id 110"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllGamesSuccess() throws Exception {
        given(this.gameService.findAll()).willReturn(this.games);

        this.mockMvc.perform(get(this.baseUrl + "/gameSchedule/games").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data").value(Matchers.hasSize(this.games.size())))
                .andExpect(jsonPath("$.data[0].gameId").value(101))
                .andExpect(jsonPath("$.data[0].opponent").value("Oklahoma Sooners"))
                .andExpect(jsonPath("$.data[1].gameId").value(102))
                .andExpect(jsonPath("$.data[1].opponent").value("Baylor Bears"));
    }

    @Test
    void testUpdateGameSuccess() throws Exception {

        Schedule s1 = new Schedule();
        s1.setId(1);

        GameDto newGameDto = new GameDto(106,
                1,
                LocalDateTime.of(LocalDate.of(2025, 2, 20), LocalTime.of(12, 0)),
                "Home",
                "New School",
                false
        );

        String json = this.objectMapper.writeValueAsString(newGameDto);

        Game updatedGame = new Game();
        updatedGame.setGameId(102);
        updatedGame.setSchedule(s1);
        updatedGame.setGameDateTime(LocalDateTime.of(LocalDate.of(2025, 10, 5), LocalTime.of(12, 0)));
        updatedGame.setVenue("Amon G. Carter Stadium");
        updatedGame.setOpponent("Baylor Bears");
        updatedGame.setFinalized(false);

        given(this.gameService.update(eq(102), Mockito.any(Game.class))).willReturn(updatedGame);
        given(scheduleRepository.findById(1)).willReturn(Optional.of(s1));

        this.mockMvc.perform(put(this.baseUrl + "/gameSchedule/game/102").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.gameId").value(updatedGame.getGameId()))
                .andExpect(jsonPath("$.data.venue").value(updatedGame.getVenue()))
                .andExpect(jsonPath("$.data.opponent").value(updatedGame.getOpponent()))
                .andExpect(jsonPath("$.data.finalized").value(updatedGame.isFinalized()));

    }

    @Test
    void testFindGamesByScheduleIdSuccess() throws Exception {
        given(this.gameService.findGamesByScheduleId(1)).willReturn(this.games);

        this.mockMvc.perform(get(this.baseUrl + "/gameSchedule/1/games").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data").value(Matchers.hasSize(this.games.size())))
                .andExpect(jsonPath("$.data[0].gameId").value(101))
                .andExpect(jsonPath("$.data[0].opponent").value("Oklahoma Sooners"))
                .andExpect(jsonPath("$.data[1].gameId").value(102))
                .andExpect(jsonPath("$.data[1].opponent").value("Baylor Bears"));


    }

}
