package pinup.backend.conquer.command.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pinup.backend.conquer.command.domain.entity.Region;
import pinup.backend.conquer.command.domain.entity.Territory;
import pinup.backend.member.command.domain.Users;


public interface TerritoryRepository extends JpaRepository<Territory, Long> {
    boolean existsByUserIdAndRegion(Users userId, Region region);

}
