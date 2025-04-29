package edu.tcu.cs.frogcrew.notification.converter;

import edu.tcu.cs.frogcrew.notification.Notification;
import edu.tcu.cs.frogcrew.notification.dto.NotificationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NotificationDtoToNotificationConverter implements Converter<NotificationDto, Notification> {
    @Override
    public Notification convert(NotificationDto source) {
        Notification notification = new Notification();
        notification.setNotificationId(source.notificationId());
        notification.setMessage(source.message());
        notification.setRead(source.read());
        notification.setDate(source.date());
        return notification;
    }
}
