package edu.tcu.cs.frogcrew.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.frogcrew.system.StatusCode;
import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class NotificationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    NotificationService notificationService;

    @Autowired
    ObjectMapper objectMapper;

    List<Notification> notifications;

    @Value("/api/v1")
    String baseUrl;

    private Notification n1;
    private Notification n2;

    private FrogCrewUser u1;

    @BeforeEach
    void setUp() {
        notifications = new ArrayList<>();

        u1 = new FrogCrewUser();
        u1.setId(1);
        u1.setEmail("test@tcu.edu");

        n1 = new Notification();
        n1.setNotificationId(1);
        n1.setMessage("New assignment");
        n1.setUser(u1);
        n1.setDate(LocalDateTime.now());
        n1.setRead(false);

        n2 = new Notification();
        n2.setNotificationId(2);
        n2.setMessage("Next assignment");
        n2.setUser(u1);
        n2.setDate(LocalDateTime.now());
        n2.setRead(false);

        notifications.add(n1);
        notifications.add(n2);
    }

    @Test
    void testFindNotificationByUserIdSuccess() throws Exception {
        given(this.notificationService.findNotificationsByUserId(1)).willReturn(notifications);

        this.mockMvc.perform(get(baseUrl + "/notifications/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data").value(Matchers.hasSize(this.notifications.size())))
                .andExpect(jsonPath("$.data[0].notificationId").value(1))
                .andExpect(jsonPath("$.data[0].message").value("New assignment"))
                .andExpect(jsonPath("$.data[1].notificationId").value(2))
                .andExpect(jsonPath("$.data[1].message").value("Next assignment"));

    }

    @Test
    void testFindNotificationByUserIdNotFound() throws Exception {
        given(this.notificationService.findNotificationsByUserId(2)).willThrow(new ObjectNotFoundException("user", 2));

        this.mockMvc.perform(get(baseUrl + "/notifications/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id 2"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testMarkAsReadSuccess() throws Exception {
        n1.setRead(true);
        given(this.notificationService.markAsRead(1)).willReturn(n1);

        this.mockMvc.perform(put(baseUrl + "/notifications/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Mark Success"))
                .andExpect(jsonPath("$.data.notificationId").value(1))
                .andExpect(jsonPath("$.data.read").value(true));

    }

    @Test
    void testDeleteSuccess() throws Exception {
        doNothing().when(this.notificationService).delete(1);

        this.mockMvc.perform(delete(baseUrl + "/notifications/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        doThrow(new ObjectNotFoundException("notification", 3)).when(this.notificationService).delete(3);

        this.mockMvc.perform(delete(baseUrl + "/notifications/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find notification with id 3"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}
