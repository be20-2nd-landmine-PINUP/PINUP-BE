package pinup.backend.feed.common.exception;

public class DuplicateLikeException extends RuntimeException {
    public DuplicateLikeException(Long feedId, Long userId) {
        super("이미 좋아요한 피드입니다. feedId=" + feedId + ", userId=" + userId);
    }
}
