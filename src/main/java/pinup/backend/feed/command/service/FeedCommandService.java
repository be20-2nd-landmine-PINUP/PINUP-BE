package pinup.backend.feed.command.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.feed.command.dto.FeedCreateRequest;
import pinup.backend.feed.command.dto.FeedUpdateRequest;
import pinup.backend.feed.command.entity.Feed;
import pinup.backend.feed.command.repository.FeedRepository;
import pinup.backend.feed.common.exceptioRes.FeedNotFoundException;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedCommandService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    public Long createFeed(FeedCreateRequest request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Feed feed = Feed.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .build();

            feedRepository.save(feed);
        return feed.getFeedId();
    }

    public void updateFeed(Long feedId, FeedUpdateRequest request) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFoundException(feedId));

        feed.update(request.getTitle(), request.getContent(), request.getImageUrl());
    }

    public void deleteFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new FeedNotFoundException(feedId));

        feedRepository.delete(feed);
    }
}
