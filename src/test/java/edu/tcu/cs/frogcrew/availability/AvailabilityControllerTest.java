package edu.tcu.cs.frogcrew.availability;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrew.availability.dto.AvailabilityGameDto;
import edu.tcu.cs.frogcrew.game.Game;
import edu.tcu.cs.frogcrew.game.GameRepository;
import edu.tcu.cs.frogcrew.schedule.Schedule;
import edu.tcu.cs.frogcrew.system.DBDataInitializer;
import edu.tcu.cs.frogcrew.system.StatusCode;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.FrogCrewUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AvailabilityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AvailabilityService availabilityService;

    @MockitoBean
    DBDataInitializer dbDataInitializer;

    @MockitoBean
    FrogCrewUserRepository frogCrewUserRepository;

    @MockitoBean
    GameRepository gameRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Value("/api/v1")
    String baseUrl;

    AvailabilityGameDto availabilityGameDto;
    Availability availability;
    AvailabilityId availabilityId;
    FrogCrewUser user;
    Game game;
    Schedule schedule;

    @BeforeEach
    void setUp() {
        user = new FrogCrewUser();
        user.setId(1);

        schedule = new Schedule();
        schedule.setId(1);
        schedule.setSeason("Spring 2025");

        game = new Game();
        game.setGameId(1);
        game.setSchedule(schedule);

        availabilityId = new AvailabilityId();
        availabilityId.setUserId(1);
        availabilityId.setGameId(1);

        availability = new Availability();
        availability.setId(availabilityId);
        availability.setUser(user);
        availability.setGame(game);
        availability.setAvailable(true);
        availability.setComment("I’m good to go!");

        availabilityGameDto = new AvailabilityGameDto(
                1,
                1,
                true,
                "available"
        );
    }

    @Test
    void testAddAvailabilitySuccess() throws Exception {
        given(this.gameRepository.save(Mockito.any(Game.class))).willReturn(game);
        given(this.availabilityService.save(Mockito.any(Availability.class))).willReturn(availability);
        given(this.frogCrewUserRepository.findById(1)).willReturn(Optional.of(user));
        given(this.gameRepository.findById(1)).willReturn(Optional.of(game));

        String json = objectMapper.writeValueAsString(availabilityGameDto);

        this.mockMvc.perform(post(baseUrl + "/availability").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.gameId").value(1))
                .andExpect(jsonPath("$.data.availability").value(true))
                .andExpect(jsonPath("$.data.comment").value("I’m good to go!"));

    }

    @Test
    void testUpdateAvailabilitySuccess() throws Exception {
        Availability updatedAvailability = new Availability();
        updatedAvailability.setId(availabilityId);
        updatedAvailability.setUser(user);
        updatedAvailability.setGame(game);
        updatedAvailability.setAvailable(false);
        updatedAvailability.setComment("I’m unavailable now!");

        given(this.frogCrewUserRepository.findById(1)).willReturn(Optional.of(user));
        given(this.gameRepository.findById(1)).willReturn(Optional.of(game));
        given(this.availabilityService.update(Mockito.any(Availability.class), eq(availabilityId))).willReturn(updatedAvailability);

        // Create the request DTO (no availabilityId in response)
        AvailabilityGameDto updateDto = new AvailabilityGameDto(1, 1, false, "I’m unavailable now!");

        String json = objectMapper.writeValueAsString(updateDto);

        this.mockMvc.perform(put(baseUrl + "/availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.gameId").value(1))
                .andExpect(jsonPath("$.data.availability").value(false))
                .andExpect(jsonPath("$.data.comment").value("I’m unavailable now!"));
    }

    @Test
    void testFindAvailabilityByUserIdAndScheduleIdSuccess() throws Exception {
        given(this.availabilityService.findByUserIdAndScheduleId(1, 1)).willReturn(List.of(availability));

        this.mockMvc.perform(get(baseUrl + "/availability/1/schedule/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data[0].userId").value(1))
                .andExpect(jsonPath("$.data[0].scheduleId").value(1))
                .andExpect(jsonPath("$.data[0].availability").value(true));

    }

    @Test
    void testFindAvailabilityByUserIdAndScheduleIdWhereUserIdNotFound() throws Exception {
        given(this.availabilityService.findByUserIdAndScheduleId(99, 1)).willThrow(new ObjectNotFoundException("user", 99));

        this.mockMvc.perform(get(baseUrl + "/availability/99/schedule/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id 99"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAvailabilityByUserIdAndScheduleIdWhereScheduleIdNotFound() throws Exception {
        given(this.availabilityService.findByUserIdAndScheduleId(1, 99)).willThrow(new ObjectNotFoundException("schedule", 99));

        this.mockMvc.perform(get(baseUrl + "/availability/1/schedule/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find schedule with id 99"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAvailabilityByUserIdAndSeasonSuccess() throws Exception {
        given(this.availabilityService.findByUserIdAndSeason(1, "Spring 2025")).willReturn(List.of(availability));

        this.mockMvc.perform(get(baseUrl + "/availability/1/season/Spring 2025").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data[0].userId").value(1))
                .andExpect(jsonPath("$.data[0].scheduleId").value(1))
                .andExpect(jsonPath("$.data[0].availability").value(true));


    }

}
