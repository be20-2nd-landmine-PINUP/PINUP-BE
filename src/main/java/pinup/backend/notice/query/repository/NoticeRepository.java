package pinup.backend.notice.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.notice.query.entity.Notice;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Notice findByNoticeId(Integer noticeId);
}
