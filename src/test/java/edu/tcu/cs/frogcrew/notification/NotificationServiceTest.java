package edu.tcu.cs.frogcrew.notification;

import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import edu.tcu.cs.frogcrew.user.FrogCrewUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    NotificationRepository notificationRepository;

    @Mock
    FrogCrewUserRepository frogCrewUserRepository;

    @InjectMocks
    NotificationService notificationService;

    Notification n1;
    Notification n2;
    FrogCrewUser user;

    @BeforeEach
    void setUp() {
        user = new FrogCrewUser();
        user.setId(1);
        user.setEmail("test@tcu.edu");

        n1 = new Notification();
        n1.setNotificationId(1);
        n1.setMessage("New assignment");
        n1.setUser(user);
        n1.setDate(LocalDateTime.now());
        n1.setRead(false);

        n2 = new Notification();
        n2.setNotificationId(2);
        n2.setMessage("Next assignment");
        n2.setUser(user);
        n2.setDate(LocalDateTime.now());
        n2.setRead(false);
    }

    @Test
    void testFindByIdSuccess() {
        given(this.notificationRepository.findById(1)).willReturn(Optional.of(n1));

        Notification found = this.notificationService.findById(1);

        assertThat(found.getNotificationId()).isEqualTo(1);
        assertThat(found.getMessage()).isEqualTo("New assignment");
        verify(notificationRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        given(this.notificationRepository.findById(1)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> this.notificationService.findById(1));

        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
        verify(notificationRepository, times(1)).findById(1);
    }

    @Test
    void testFindNotificationByUserId() {
        given(this.notificationRepository.findByFrogCrewUserId(1)).willReturn(List.of(n1, n2));

        List<Notification> notifications = this.notificationService.findNotificationsByUserId(1);

        assertThat(notifications.size()).isEqualTo(2);
        assertThat(notifications.get(0).getNotificationId()).isEqualTo(1);
        verify(notificationRepository, times(1)).findByFrogCrewUserId(1);
    }

    @Test
    void testFindNotificationByUserIdNotFound() {
        given(this.notificationRepository.findByFrogCrewUserId(2)).willReturn(List.of());

        List<Notification> notifications = this.notificationService.findNotificationsByUserId(2);

        assertThat(notifications).isEmpty();
        verify(notificationRepository, times(1)).findByFrogCrewUserId(2);
    }

    @Test
    void testMarkAsReadSuccess() {
        given(this.notificationRepository.findById(1)).willReturn(Optional.of(n1));
        given(this.notificationRepository.save(n1)).willReturn(n1);

        this.notificationService.markAsRead(1);

        assertThat(n1.isRead()).isTrue();
        verify(notificationRepository, times(1)).findById(1);
        verify(notificationRepository, times(1)).save(n1);
    }

    @Test
    void testDeleteSuccess() {
        given(this.notificationRepository.findById(1)).willReturn(Optional.of(n1));

        this.notificationService.delete(1);

        verify(notificationRepository, times(1)).findById(1);
        verify(notificationRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        given(this.notificationRepository.findById(3)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> this.notificationService.delete(3));
    }

}
