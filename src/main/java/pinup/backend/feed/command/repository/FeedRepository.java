package pinup.backend.feed.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pinup.backend.feed.command.entity.Feed;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
}
