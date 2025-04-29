package edu.tcu.cs.frogcrew.notification.converter;

import edu.tcu.cs.frogcrew.notification.Notification;
import edu.tcu.cs.frogcrew.notification.dto.NotificationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NotificationToNotificationDtoConverter implements Converter<Notification, NotificationDto> {
    @Override
    public NotificationDto convert(Notification source) {
        return new NotificationDto(source.getNotificationId(), source.getMessage(), source.isRead(), source.getDate());
    }
}
