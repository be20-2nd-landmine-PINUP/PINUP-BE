package pinup.backend.feed.common.exception;

public class FeedNotFoundException extends RuntimeException {
    public FeedNotFoundException(Long feedId) {
        super("해당 피드를 찾을 수 없습니다. feedId=" + feedId);
    }
}
