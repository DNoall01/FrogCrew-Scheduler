package edu.tcu.cs.frogcrew.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.dto.GameDto;
import edu.tcu.cs.frogcrew.schedule.dto.ScheduleDto;
import edu.tcu.cs.frogcrew.system.StatusCode;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ScheduleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ScheduleService scheduleService;

    @Autowired
    ObjectMapper objectMapper;

    List<Schedule> schedules;
    List<String> sports;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    Schedule s1;
    Schedule s2;

    @BeforeEach
    void setUp() {
        this.schedules = new ArrayList<>();
        this.sports = new ArrayList<>();

        s1 = new Schedule();
        s1.setId(1);
        s1.setSport("Football");
        s1.setSeason("Spring 2025");

        s2 = new Schedule();
        s2.setId(2);
        s2.setSport("Soccer");
        s2.setSeason("Spring 2025");

        Schedule s3 = new Schedule();
        s3.setId(3);
        s3.setSport("Football");
        s3.setSeason("Fall 2025");

        Game game = new Game();
        game.setGameId(1);
        game.setSchedule(s1);
        game.setSport(s1.getSport());
        game.setVenue("Home");
        game.setOpponent("OU");
        game.setFinalized(false);
        game.setGameDateTime(LocalDateTime.now());

        s1.setGames(List.of(game));

        schedules.add(s1);
        schedules.add(s2);

        sports.add("Football");
        sports.add("Soccer");
    }

    @Test
    void testAddGameToScheduleSuccess() throws Exception {
        GameDto gameDto = new GameDto(null, 1, LocalDateTime.now(), "Amon G. Carter Stadium", "OU", false);

        Schedule schedule = new Schedule();
        schedule.setId(1);

        Game savedGame = new Game();
        savedGame.setGameId(100);
        savedGame.setSchedule(schedule);
        savedGame.setSport("Football");
        savedGame.setVenue("Amon G. Carter Stadium");
        savedGame.setOpponent("OU");
        savedGame.setFinalized(false);
        savedGame.setGameDateTime(LocalDateTime.now());

        given(this.scheduleService.addGameToSchedule(eq(1), Mockito.any(Game.class))).willReturn(savedGame);

        String json = this.objectMapper.writeValueAsString(gameDto);

        this.mockMvc.perform(post(baseUrl + "/gameSchedule/1/games").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.gameId").value(100))
                .andExpect(jsonPath("$.data.venue").value("Amon G. Carter Stadium"))
                .andExpect(jsonPath("$.data.opponent").value("OU"))
                .andExpect(jsonPath("$.data.finalized").value(false));
    }

    @Test
    void testAddGameScheduleSuccess() throws Exception {
        ScheduleDto dto = new ScheduleDto(null, "Football", "Spring 2025");

        Schedule savedSchedule = new Schedule();
        savedSchedule.setId(1);
        savedSchedule.setSport("Football");
        savedSchedule.setSeason("Spring 2025");

        given(this.scheduleService.save(Mockito.any(Schedule.class))).willReturn(savedSchedule);

        String json = this.objectMapper.writeValueAsString(dto);

        this.mockMvc.perform(post(baseUrl + "/gameSchedule").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.sport").value("Football"))
                .andExpect(jsonPath("$.data.season").value("Spring 2025"));
    }

    @Test
    void testFindGameScheduleByScheduleIdSuccess() throws Exception {
        given(this.scheduleService.findById(1)).willReturn(s1);

        this.mockMvc.perform(get(baseUrl + "/gameSchedule/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.sport").value("Football"))
                .andExpect(jsonPath("$.data.season").value("Spring 2025"));
    }

    @Test
    void testFindGameScheduleByScheduleIdNotFound() throws Exception {
        given(this.scheduleService.findById(99)).willThrow(new ObjectNotFoundException("schedule", 99));

        this.mockMvc.perform(get(this.baseUrl + "/gameSchedule/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find schedule with id 99"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testUpdateGameScheduleSuccess() throws Exception {}

    @Test
    void testUpdateGameScheduleIdNotFound() throws Exception {}

    @Test
    void testFindGameSchedulesBySeasonSuccess() throws Exception {
        given(this.scheduleService.findBySeason("Spring 2025")).willReturn(this.schedules);

        this.mockMvc.perform(get(baseUrl + "/gameSchedule/season/Spring 2025").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].sport").value("Football"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].sport").value("Soccer"));

    }

    @Test
    void testPublishGameScheduleSuccess() throws Exception {}

    @Test
    void testFindAllSportsSuccess() throws Exception {
        given(this.scheduleService.getAllSports()).willReturn(this.sports);

        this.mockMvc.perform(get(baseUrl + "/gameSchedule/sports").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data[0]").value("Football"))
                .andExpect(jsonPath("$.data[1]").value("Soccer"));
    }
}
