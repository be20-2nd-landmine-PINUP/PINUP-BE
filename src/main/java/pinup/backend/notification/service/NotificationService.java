package pinup.backend.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pinup.backend.notification.dto.NotificationRequest;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final Map<Integer, SseEmitter> sseEmitter;

    public SseEmitter establishConnect(Integer clientId) {
        Long connectionTimeout = 60 * 1000L;

        SseEmitter emitter = new SseEmitter(connectionTimeout);

        sseEmitter.put(clientId, emitter);

        emitter.onTimeout(() -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("timeout")
                        .data("연결 유지 시간 초과"));
            } catch (IOException ignored) {}
            emitter.complete();  // 이 호출로 onCompletion() 자동 실행됨
        });

        emitter.onCompletion(() -> sseEmitter.remove(clientId));
        emitter.onError(e -> sseEmitter.remove(clientId));

        try {
            emitter.send(SseEmitter.event().name("connect").data("연결 성공"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public void sendNotification(Integer clientId, NotificationRequest notificationRequest) {
        SseEmitter emitter = sseEmitter.get(clientId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("new notification")
                        .data(notificationRequest));
            }
            catch (IOException e) {
                sseEmitter.remove(clientId);
                emitter.completeWithError(e);
            }
        }
    }
}
