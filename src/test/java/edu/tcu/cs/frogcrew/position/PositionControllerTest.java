package edu.tcu.cs.frogcrew.position;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrew.position.dto.PositionDto;
import edu.tcu.cs.frogcrew.system.StatusCode;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PositionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PositionService positionService;

    @Autowired
    ObjectMapper objectMapper;

    List<Position> positions;

    @Value("/api/v1")
    String baseUrl;

    Position p1;
    Position p2;

    @BeforeEach
    void setUp() {
        this.positions = new ArrayList<>();

        p1 = new Position();
        p1.setPositionId(1);
        p1.setPositionName("Team Lead");
        p1.setPositionLocation("HQ");

        p2 = new Position();
        p2.setPositionId(2);
        p2.setPositionName("Referee");
        p2.setPositionLocation("End Zone");

        positions.add(p1);
        positions.add(p2);
    }

    @Test
    void testFindAllPositionsSuccess() throws Exception {
        given(this.positionService.findAllPositionNames()).willReturn(List.of(p1.getPositionName(), p2.getPositionName()));

        this.mockMvc.perform(get(this.baseUrl + "/positions").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data[0]").value(p1.getPositionName()))
                .andExpect(jsonPath("$.data[1]").value(p2.getPositionName()));
    }

    @Test
    void testAddPositionSuccess() throws Exception {
        PositionDto newPositionDto = new PositionDto(3,
                "Crew"
        );

        String json = this.objectMapper.writeValueAsString(newPositionDto);

        Position savedPosition = new Position();
        savedPosition.setPositionId(3);
        savedPosition.setPositionName("Crew");
        savedPosition.setPositionLocation("Sideline");

        given(this.positionService.save(Mockito.any(Position.class))).willReturn(savedPosition);

        this.mockMvc.perform(post(this.baseUrl + "/positions").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.positionId").value(savedPosition.getPositionId()))
                .andExpect(jsonPath("$.data.positionName").value(savedPosition.getPositionName()));

    }

    @Test
    void testUpdatePositionSuccess() throws Exception {
        PositionDto updateDto = new PositionDto(p1.getPositionId(), "Updated Team Lead");

        Position updated = new Position();
        updated.setPositionId(p1.getPositionId());
        updated.setPositionName("Updated Team Lead");
        updated.setPositionLocation("HQ");

        given(this.positionService.update(eq(p1.getPositionId()), Mockito.any(Position.class))).willReturn(updated);

        String json = this.objectMapper.writeValueAsString(updateDto);

        this.mockMvc.perform(put(this.baseUrl + "/positions/" + p1.getPositionId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.positionId").value(p1.getPositionId()))
                .andExpect(jsonPath("$.data.positionName").value("Updated Team Lead"));
    }

    @Test
    void testUpdatePositionNotFound() throws Exception {
        PositionDto updateDto = new PositionDto(99, "Ghost Role");

        given(this.positionService.update(eq(99), Mockito.any(Position.class)))
                .willThrow(new ObjectNotFoundException("position", 99));

        String json = this.objectMapper.writeValueAsString(updateDto);

        this.mockMvc.perform(put(this.baseUrl + "/positions/99").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND)) // You might need to define this
                .andExpect(jsonPath("$.message").value("Could not find position with id 99"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}
