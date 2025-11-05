package pinup.backend.notice.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.notice.command.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
