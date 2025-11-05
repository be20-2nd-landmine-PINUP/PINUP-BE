package pinup.backend.feed.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pinup.backend.member.command.domain.Users;

import java.time.LocalDateTime;

@Entity
@Table(name = "feed")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long feedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @CreationTimestamp      // 생성 시 자동 기입 예정
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp        // 수정 시 자동 기입 예정
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Feed(Users user, String title, String content, String imageUrl) {
        this.userId = user;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    // 피드 수정 간 사용할 메소드
    public void update(String title, String content, String imageUrl) {
        if (title != null && !title.isBlank()) this.title = title;
        if (content != null && !content.isBlank()) this.content = content;
        if (imageUrl != null && !imageUrl.isBlank()) this.imageUrl = imageUrl;
    }
}
