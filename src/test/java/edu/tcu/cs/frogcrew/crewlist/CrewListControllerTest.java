package edu.tcu.cs.frogcrew.crewlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrew.creweduser.converter.CrewedUserToCrewMemberDtoConverter;
import edu.tcu.cs.frogcrew.crewlist.converter.GameToCrewListDtoConverter;
import edu.tcu.cs.frogcrew.crewlist.dto.CrewListDto;
import edu.tcu.cs.frogcrew.system.StatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CrewListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrewListService crewListService;

    @MockitoBean
    private GameToCrewListDtoConverter gameToCrewListDtoConverter;

    @MockitoBean
    private CrewedUserToCrewMemberDtoConverter crewedUserToCrewMemberDtoConverter;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    CrewListDto crewListDto;

    @BeforeEach
    void setup() {
        crewListDto = new CrewListDto(
                1,
                LocalTime.of(7, 30),
                LocalDate.of(2025, 5, 5),
                "Amon Carter Stadium",
                "Texas",
                List.of()
        );
    }

    @Test
    void testFindCrewListByGameIdSuccess() throws Exception {
        // Arrange
        given(this.crewListService.findCrewListByGameId(1)).willReturn(crewListDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/crewList/1"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data.gameId").value(1))
                .andExpect(jsonPath("$.data.venue").value("Amon Carter Stadium"))
                .andExpect(jsonPath("$.data.opponent").value("Texas"));
    }
}
