package pinup.backend.member.command.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.MemberCommandRepository;
import pinup.backend.member.command.repository.UserRepository;
import pinup.backend.notification.dto.NotificationRequest;
import pinup.backend.notification.service.NotificationService;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final MemberCommandRepository memberCommandRepository;
    private final NotificationService notificationService; // 알림 주입

    // 회원 정지
    public void suspendUser(Long id) {
        Users user = memberCommandRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        // 회원 상태 변경
        user.suspend();

        // SSE 알림 전송
        notificationService.sendNotification(id, NotificationRequest.builder()
                .senderId(null)
                .receiverId(id)
                .notificationType(NotificationRequest.NotificationType.SUSPEND)
                .notificationMessage("당신은 활동 정지 되었습니다.")
                .build()); // sendNotification(보낼 유저 id, 메시지 내용 dto)
    }

    // 회원 활성화
    public void activateUser(Long id) {
        Users user = memberCommandRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        user.activate();
    }

    // 회원 삭제
    public void deleteUser(Long id) {
        Users user = memberCommandRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        user.delete();
    }

    // 로그인한 사용자의 user_id 조회 (email → user_id 변환)
    // 로그인 사용자 ID 조회
    public Long getLoginUserId(Principal principal) {
        String email = principal.getName();
        Users user = memberCommandRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return user.getUserId();
    }
}
