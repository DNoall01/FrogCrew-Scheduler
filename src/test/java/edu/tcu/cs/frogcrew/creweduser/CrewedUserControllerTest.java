package edu.tcu.cs.frogcrew.creweduser;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrew.system.StatusCode;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.converter.FrogCrewUserToFrogCrewedUserDtoConverter;
import edu.tcu.cs.frogcrew.user.dto.FrogCrewedUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CrewedUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrewedUserService crewedUserService;

    @MockitoBean
    private FrogCrewUserToFrogCrewedUserDtoConverter frogCrewUserToFrogCrewedUserDtoConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    FrogCrewUser frogUser;
    FrogCrewedUserDto frogCrewedUserDto;

    @BeforeEach
    void setUp() {
        frogUser = new FrogCrewUser();
        frogUser.setId(1);
        frogUser.setFirstName("John");
        frogUser.setLastName("Smith");
        frogUser.setEmail("john.smith@gmail.com");

        frogCrewedUserDto = new FrogCrewedUserDto(
                frogUser.getId(),
                frogUser.getFirstName() + " " + frogUser.getLastName()
        );
    }

    @Test
    void testFindCrewMembersByAvailabilityAndPosition_Success() throws Exception {
        // Arrange
        Integer gameId = 1;
        String positionName = "Director";

        given(this.crewedUserService.findCrewedUsersByAvailabilityAndPosition(gameId, positionName))
                .willReturn(List.of(frogUser));
        given(this.frogCrewUserToFrogCrewedUserDtoConverter.convert(frogUser))
                .willReturn(frogCrewedUserDto);

        // Act & Assert
        this.mockMvc.perform(get(this.baseUrl + "/CrewedUser/{gameId}/{position}", gameId, positionName).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data[0].userId").value(frogCrewedUserDto.userId()))
                .andExpect(jsonPath("$.data[0].fullName").value(frogCrewedUserDto.fullName()));

        verify(this.crewedUserService).findCrewedUsersByAvailabilityAndPosition(gameId, positionName);
    }

}
