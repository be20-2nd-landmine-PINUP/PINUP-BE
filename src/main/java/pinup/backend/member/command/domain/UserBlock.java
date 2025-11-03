package pinup.backend.member.command.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_block")
public class UserBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long blockId;

    @Column(name = "blocker_id", nullable = false)
    private Long blockerId;  // 차단을 건 사람 (로그인 사용자)

    @Column(name = "blocked_id", nullable = false)
    private Long blockedId;  // 차단당한 사람

    @Column(name = "is_active", nullable = false)
    private boolean active = true; // 차단 상태 유지 여부


}
