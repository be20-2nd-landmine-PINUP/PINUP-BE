package pinup.backend.store.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.domain.Store;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    // 유저 인벤토리 아이템 조회
    List<Inventory> findAllByUsers(Users userId);

    // 유저의 장착 중인 아이템 조회
    List<Inventory> findByUsers_UserIdAndIsEquippedTrue(Long users);

    // 유저의 특정 아이템 보유 확인
    boolean existsByUsersAndStore(Users users, Store store);

}
