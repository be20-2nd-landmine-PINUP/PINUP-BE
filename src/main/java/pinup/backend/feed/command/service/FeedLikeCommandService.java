package pinup.backend.feed.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.feed.command.entity.Feed;
import pinup.backend.feed.command.entity.FeedLike;
import pinup.backend.feed.command.entity.FeedLikeId;
import pinup.backend.feed.command.repository.FeedLikeRepository;
import pinup.backend.feed.command.repository.FeedRepository;
import pinup.backend.feed.common.exception.DuplicateLikeException;
import pinup.backend.feed.common.exception.FeedNotFoundException;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedLikeCommandService {

    private static final int POINT_PER_LIKE = 1; // ← 필요 시 설정값으로 교체

    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final UserRepository userRepository;
    // private final PointLogRepository pointLogRepository;
    // private final TotalPointRepository totalPointRepository;

    public record LikeResult(boolean liked, long likeCount) {}

    public LikeResult like(Long feedId, Long userId) throws DuplicateLikeException {
        // 존재 확인
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFoundException(feedId));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. userId=" + userId));

        // 서비스단 빠른 중복 차단
        boolean already = feedLikeRepository
                .existsByFeedLikeId_UserIdAndFeedLikeId_FeedId(userId, feedId);
        if (already) {
            throw new DuplicateLikeException(feedId, userId);
        }

        // 저장 시도 (PK (user_id, feed_id)로 최종 중복 방어)
        try {
            FeedLike like = FeedLike.builder()
                    .feedLikeId(new FeedLikeId(userId, feedId))
                    .userId(user)
                    .feedId(feed)
                    .build();

            feedLikeRepository.save(like);

            // 카운트 증가
            feedRepository.incrementLikeCount(feedId);

            // 포인트 지급 (피드 작성자에게) — 최초 성공시에만
            Long authorId = feed.getUserId().getUserId();
            pointLogRepository.insertLikePoint(authorId, feedId, POINT_PER_LIKE);
            totalPointRepository.increment(authorId, POINT_PER_LIKE);

        } catch (DataIntegrityViolationException e) {
            // 경합으로 중복 insert 발생 → 중복 좋아요로 간주
            throw new DuplicateLikeException(feedId, userId);
        }

        long cnt = feedLikeRepository.countByFeedLikeId_FeedId(feedId);
        return new LikeResult(true, cnt);
    }
}
