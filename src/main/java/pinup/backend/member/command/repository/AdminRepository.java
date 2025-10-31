package pinup.backend.member.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.member.command.domain.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByName(String name);
}
