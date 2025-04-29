package edu.tcu.cs.frogcrew.notification.dto;

import java.time.LocalDateTime;

public record NotificationDto(Integer notificationId,
                              String message,
                              boolean read,
                              LocalDateTime date) {
}
