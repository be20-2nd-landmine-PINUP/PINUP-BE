package pinup.backend.member.command.domain;

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
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false, length = 10)
    private LoginType loginType; // GOOGLE, KAKAO, NAVER

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

    // 회원 정보 수정
    public void updateInfo(
            String nickname, Gender gender, PreferredCategory preferredCategory, PreferredSeason preferredSeason, LocalDate birthDate) {
        this.nickname = nickname;
        this.gender = gender;
        this.preferredCategory = preferredCategory;
        this.preferredSeason = preferredSeason;
        this.birthDate = birthDate;
    }


    // ENUM 정의
    public enum LoginType {
        GOOGLE, KAKAO, NAVER
    }

    public enum Gender {
        M, F, U // U는 Unknown > google 로그인시 성별 제공이 안되므로 U로 기본 설정
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

    // 상태 변경 메서드 추가
    public void suspend() { this.status = Status.SUSPENDED; }
    public void activate() { this.status = Status.ACTIVE; }
    public void delete() { this.status = Status.DELETED; }
}
