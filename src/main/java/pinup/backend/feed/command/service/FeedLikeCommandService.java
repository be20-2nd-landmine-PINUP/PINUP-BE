package pinup.backend.feed.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
import pinup.backend.point.command.service.PointService;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedLikeCommandService {

    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final UserRepository userRepository;
    private final PointService pointService; // 우선은 서비스 직접 등록

    public record LikeResult(boolean liked, long likeCount) {}

    public LikeResult like(Long feedId, Long userId) {
        // 존재 확인
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFoundException(feedId));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. userId=" + userId));

        // 서비스단 중복 차단
        boolean already = feedLikeRepository
                .existsByFeedLikeId_UserIdAndFeedLikeId_FeedId(userId, feedId);
        if (already) {
            throw new DuplicateLikeException(feedId, userId);
        }

        try {
            FeedLike like = FeedLike.builder()
                    .feedLikeId(new FeedLikeId(userId, feedId))
                    .userId(user)
                    .feedId(feed)
                    .build();

            feedLikeRepository.save(like);

            // 카운트 증가
            feedRepository.incrementLikeCount(feedId);

            // 좋아요 포인트 지급
            Long authorId = feed.getUserId().getUserId();
            pointService.grantLike(authorId, feedId);

        } catch (DataIntegrityViolationException e) {
            // 중복 insert 발생은 중복 좋아요로 간주
            throw new DuplicateLikeException(feedId, userId);
        }

        return new LikeResult(true, feed.getLikeCount());
    }
}
