package pinup.backend.feed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "feed_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedLike {

    @EmbeddedId
    private FeedLikeId id;

    /*
    TODO: 실제 사용할 엔티티 타입을 기재 후 해당 패키지 import 까먹지 말기
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("users")
    @JoinColumn(name = "user_id", nullable = false)
    private {실제 엔티티 타입} userId;
    */

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("feed")
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feedId;

    @CreationTimestamp      //생성 시 자동 기입 예정
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdDate;

    /*
    TODO: 위와 동일, 실제 사용할 엔티티 타입 기준으로 작성 필요
    // 복합키 전용 생성자(빌더 형식으로 사용) 선언
    @Builder
    public FeedLike(FeedLikeId id, {실제 엔티티 타입} userId, Feed feedId, LocalDateTime createdDate) {
        this.id = id;
        this.userId = userId;
        this.feedId = feedId;
        this.createdDate = createdDate;
    }
    */
}
