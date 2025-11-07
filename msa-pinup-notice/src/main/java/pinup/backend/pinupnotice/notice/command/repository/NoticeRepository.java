package pinup.backend.pinupnotice.notice.command.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.pinupnotice.notice.command.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
