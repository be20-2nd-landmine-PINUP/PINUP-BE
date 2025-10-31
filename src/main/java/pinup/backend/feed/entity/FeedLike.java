package pinup.backend.feed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import pinup.backend.member.command.domain.Users;

import java.time.LocalDateTime;

@Entity
@Table(name = "feed_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedLike {

    @EmbeddedId
    private FeedLikeId feedLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("feedId")
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feedId;

    @CreationTimestamp      //생성 시 자동 기입 예정
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public FeedLike(FeedLikeId feedLikeId, Users userId, Feed feedId, LocalDateTime createdAt) {
        this.feedLikeId = feedLikeId;
        this.userId = userId;
        this.feedId = feedId;
        this.createdAt = createdAt;
    }
}
