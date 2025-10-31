package pinup.backend.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.entity.Inventory;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    /**
     *  인벤토리(보관함) 관련 Repository
     * - 유저별 아이템 조회
     * - 아이템 보유 여부 확인
     * - 장착 상태 관리
     */

    // 유저 전체 아이템 조회
    List<Inventory> findAllByUsers(Users users);

    // 유저의 장착 중인 아이템 조회
    List<Inventory> findEquippedByUserId(Long userId);

}
