package edu.tcu.cs.frogcrew.notification;

import edu.tcu.cs.frogcrew.system.exception.ObjectNotFoundException;
import edu.tcu.cs.frogcrew.user.FrogCrewUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FrogCrewUserRepository frogCrewUserRepository;

    public NotificationService(NotificationRepository notificationRepository, FrogCrewUserRepository frogCrewUserRepository) {
        this.notificationRepository = notificationRepository;
        this.frogCrewUserRepository = frogCrewUserRepository;
    }

    public Notification findById(Integer notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(() -> new ObjectNotFoundException("notification", notificationId));
    }

    public List<Notification> findNotificationsByUserId(Integer userId) {
        return notificationRepository.findByFrogCrewUserId(userId);
    }

    public Notification markAsRead(Integer notificationId) {
        Notification notification = this.notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ObjectNotFoundException("notification", notificationId));
        notification.setRead(true);
        return this.notificationRepository.save(notification);
    }

    public void delete(Integer notificationId) {
        this.notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ObjectNotFoundException("notification", notificationId));
        this.notificationRepository.deleteById(notificationId);
    }
}
