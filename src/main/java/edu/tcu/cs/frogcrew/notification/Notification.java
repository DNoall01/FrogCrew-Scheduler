package edu.tcu.cs.frogcrew.notification;

import edu.tcu.cs.frogcrew.user.FrogCrewUser;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer notificationId;

    private String message;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private FrogCrewUser frogCrewUser;

    private boolean read = false;

    public void viewNotifications() {}

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public FrogCrewUser getUser() {
        return frogCrewUser;
    }

    public void setUser(FrogCrewUser frogCrewUser) {
        this.frogCrewUser = frogCrewUser;
    }
}
