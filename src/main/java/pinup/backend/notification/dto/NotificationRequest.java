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
    private Integer senderId;
    private Integer receiverId;
    private String notificationType;
    private String notificationMessage;
}
