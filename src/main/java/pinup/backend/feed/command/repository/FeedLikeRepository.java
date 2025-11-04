package pinup.backend.feed.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pinup.backend.feed.command.entity.FeedLike;

@Repository
public interface FeedLikeRepository extends JpaRepository<FeedLike,Integer> {

    // 해당 피드 삭제 대비 전 따봉 삭제용
    void deleteAllByFeedLikeId_FeedId(Long feedId);
}
