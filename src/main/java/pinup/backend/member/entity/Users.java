package pinup.backend.member.entity;

import com.nimbusds.openid.connect.sdk.claims.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false, length = 10)
    private LoginType loginType; // GOOGLE / KAKAO

    @Column(name = "user_name", nullable = false, length = 20)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email; // 소셜 로그인용 이메일

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 1)
    private Gender gender; // M / F

    @Column(name = "profile_image", length = 255)
    private String profileImage; // 프로필 이미지 URL

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private Status status = Status.ACTIVE; // ACTIVE / SUSPENDED / DELETED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_category", nullable = false, length = 10)
    private PreferredCategory preferredCategory; // 자연 / 체험 / 역사 / 문화

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_season", nullable = false, length = 10)
    private PreferredSeason preferredSeason; // 봄 / 여름 / 가을 / 겨울

    // 자동으로 생성/수정 시간 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ENUM 정의
    public enum LoginType {
        GOOGLE, KAKAO
    }
    
    public enum Gender {
        M, F
    }

    public enum Status {
        ACTIVE, SUSPENDED, DELETED
    }

    public enum PreferredCategory {
        자연, 체험, 역사, 문화
    }

    public enum PreferredSeason {
        봄, 여름, 가을, 겨울
    }
}
