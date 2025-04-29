package edu.tcu.cs.frogcrew.notification;

import edu.tcu.cs.frogcrew.notification.converter.NotificationDtoToNotificationConverter;
import edu.tcu.cs.frogcrew.notification.converter.NotificationToNotificationDtoConverter;
import edu.tcu.cs.frogcrew.notification.dto.NotificationDto;
import edu.tcu.cs.frogcrew.system.Result;
import edu.tcu.cs.frogcrew.system.StatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationToNotificationDtoConverter notificationToNotificationDtoConverter;
    private final NotificationDtoToNotificationConverter notificationDtoToNotificationConverter;

    public NotificationController(NotificationService notificationService, NotificationToNotificationDtoConverter notificationToNotificationDtoConverter, NotificationDtoToNotificationConverter notificationDtoToNotificationConverter) {
        this.notificationService = notificationService;
        this.notificationToNotificationDtoConverter = notificationToNotificationDtoConverter;
        this.notificationDtoToNotificationConverter = notificationDtoToNotificationConverter;
    }

    @GetMapping("/{userId}")
    public Result findNotificationsByUserId(@PathVariable Integer userId) {
        List<Notification> foundNotifications = this.notificationService.findNotificationsByUserId(userId);
        List<NotificationDto> notificationDtos = foundNotifications.stream()
                .map(notificationToNotificationDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find Success", notificationDtos);
    }

    @PutMapping("/{notificationId}")
    public Result markAsRead(@PathVariable Integer notificationId) {
        Notification notification = this.notificationService.markAsRead(notificationId);
        NotificationDto notificationDto = this.notificationToNotificationDtoConverter.convert(notification);
        return new Result(true, StatusCode.SUCCESS, "Mark Success", notificationDto);
    }

    @DeleteMapping("/{notificationId}")
    public Result deleteNotification(@PathVariable Integer notificationId) {
        this.notificationService.delete(notificationId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
