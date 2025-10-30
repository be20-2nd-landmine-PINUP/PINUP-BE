package pinup.backend.feed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "feed")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private int feedId;

    /*
    TODO: 실제 사용할 엔티티 타입 기준으로 작성 필요
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private {실제 엔티티 타입} user;
    */

    @Column(name = "content", nullable = false, length = 100)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @CreationTimestamp      // 생성 시 자동 기입 예정
    @Column(name = "updated_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp        // 수정 시 자동 기입 예정
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    /*
    TODO: 실제 사용할 엔티티 타입 기준으로 작성 필요
    @Builder
    public Feed({실제 엔티티 타입} user, String title, String content, String imageUrl) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
    */
}
