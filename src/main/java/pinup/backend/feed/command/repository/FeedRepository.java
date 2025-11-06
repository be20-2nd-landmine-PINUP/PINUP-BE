package pinup.backend.feed.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pinup.backend.feed.command.entity.Feed;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE feed SET like_count = COALESCE(like_count,0) + 1 WHERE feed_id = :feedId", nativeQuery = true)
    int incrementLikeCount(@Param("feedId") Long feedId);
}
