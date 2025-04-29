package edu.tcu.cs.frogcrew.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrew.system.Role;
import edu.tcu.cs.frogcrew.system.StatusCode;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import edu.tcu.cs.frogcrew.user.dto.UserDto;
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
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class FrogCrewUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    FrogCrewUserService userService;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    List<FrogCrewUser> users;

    @BeforeEach
    void setUp() {
        this.users = new ArrayList<>();

        FrogCrewUser u1 = new FrogCrewUser();
        u1.setId(1);
        u1.setFirstName("John");
        u1.setLastName("Smith");
        u1.setEmail("john.smith@gmail.com");
        u1.setPhoneNumber("1234567890");
        u1.setPassword("password");
        u1.setEnabled(true);
        u1.setRole(Role.ADMIN);

        FrogCrewUser u2 = new FrogCrewUser();
        u2.setId(2);
        u2.setFirstName("Jane");
        u2.setLastName("Smith");
        u2.setEmail("jane.smith@gmail.com");
        u2.setPhoneNumber("0987654321");
        u2.setPassword("P@ssw0rd");
        u2.setEnabled(true);
        u2.setRole(Role.CREW);

        FrogCrewUser u3 = new FrogCrewUser();
        u3.setId(3);
        u3.setFirstName("Bob");
        u3.setLastName("Smith");
        u3.setEmail("bob.smith@gmail.com");
        u3.setPhoneNumber("55555555");
        u3.setPassword("PASSWORD");
        u3.setEnabled(true);
        u3.setRole(Role.ADMIN);

        users.add(u1);
        users.add(u2);
        users.add(u3);
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
        given(this.userService.findAll()).willReturn(users);

        this.mockMvc.perform(get(this.baseUrl + "/crewMember").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.users.size())))
                .andExpect(jsonPath("$.data[0].userId").value(1))
                .andExpect(jsonPath("$.data[0].fullName").value("John Smith"))
                .andExpect(jsonPath("$.data[1].userId").value(2))
                .andExpect(jsonPath("$.data[1].fullName").value("Jane Smith"));
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
        given(this.userService.findById(1)).willReturn(users.get(0));

        this.mockMvc.perform(get(this.baseUrl + "/crewMember/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.firstName").value("John"))
                .andExpect(jsonPath("$.data.lastName").value("Smith"));
    }

    @Test
    void testFindUserByIdNotFound() throws Exception {
        given(this.userService.findById(5)).willThrow(new ObjectNotFoundException("user", 5));

        this.mockMvc.perform(get(this.baseUrl + "/crewMember/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id 5"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddUserSuccess() throws Exception {
        FrogCrewUser u = new FrogCrewUser();
        u.setId(4);
        u.setFirstName("Dill");
        u.setLastName("Smith");
        u.setEmail("dill.smith@gmail.com");
        u.setPhoneNumber("1234567890");
        u.setPassword("password");
        u.setEnabled(true);
        u.setRole(Role.ADMIN);
        u.setQualifiedPositions(List.of("Director", "Producer"));

        String json = this.objectMapper.writeValueAsString(u);

        u.setId(4);

        given(this.userService.addCrewMember(Mockito.any(FrogCrewUser.class))).willReturn(u);

        this.mockMvc.perform(post(this.baseUrl + "/crewMember").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(4))
                .andExpect(jsonPath("$.data.firstName").value("Dill"))
                .andExpect(jsonPath("$.data.lastName").value("Smith"))
                .andExpect(jsonPath("$.data.email").value("dill.smith@gmail.com"))
                .andExpect(jsonPath("$.data.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.data.role").value(Role.ADMIN.name()))
                .andExpect(jsonPath("$.data.positions[0]").value("Director"))
                .andExpect(jsonPath("$.data.positions[1]").value("Producer"));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        UserDto userDto = new UserDto(3, "Dill", "Stein", "dill.stein@gmail.com", "123456789", Role.CREW, List.of("Director", "Producer"));

        FrogCrewUser u = new FrogCrewUser();
        u.setId(3);
        u.setFirstName("Dill");
        u.setLastName("Stein");
        u.setEmail("dill.stein@gmail.com");
        u.setPhoneNumber("123456789");
        u.setRole(Role.CREW);
        u.setQualifiedPositions(List.of("Director", "Producer"));

        String json = this.objectMapper.writeValueAsString(userDto);

        given(this.userService.updateCrewMember(eq(3), Mockito.any(FrogCrewUser.class))).willReturn(u);

        this.mockMvc.perform(put(this.baseUrl + "/crewMember/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.firstName").value("Dill"))
                .andExpect(jsonPath("$.data.lastName").value("Stein"))
                .andExpect(jsonPath("$.data.email").value("dill.stein@gmail.com"))
                .andExpect(jsonPath("$.data.phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.data.role").value(Role.CREW.name()))
                .andExpect(jsonPath("$.data.positions[0]").value("Director"))
                .andExpect(jsonPath("$.data.positions[1]").value("Producer"));
    }

    @Test
    void testUpdateUserErrorWithNonExistentId() throws Exception {
        given(this.userService.updateCrewMember(eq(5), Mockito.any(FrogCrewUser.class))).willThrow(new ObjectNotFoundException("user", 5));

        UserDto userDto = new UserDto(5, "Dill", "Stein", "dill.stein@gmail.com", "123456789", Role.CREW, List.of("Director", "Producer"));

        String json = this.objectMapper.writeValueAsString(userDto);

        this.mockMvc.perform(put(this.baseUrl + "/crewMember/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id 5"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}
