package pinup.backend.store.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.conquer.command.domain.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {
}

