package pinup.backend.feed.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

// TODO : Merge 후 접근자는 확인 요망
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
public class FeedLikeId implements Serializable {
    private String userId;
    private String feedId;
}