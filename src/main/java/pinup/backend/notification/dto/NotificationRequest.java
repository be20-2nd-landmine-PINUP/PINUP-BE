package pinup.backend.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NotificationRequest {
    private Long senderId;
    private Long receiverId;
    private NotificationType notificationType;
    private String notificationMessage;

    public enum NotificationType {
        FEED_LIKE, BONUS_POINT, SUSPEND, REPORT_HANDLED
    }
}
