package pinup.backend.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pinup.backend.store.domain.Store;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    /*
     *  상점 아이템 관련 Repository
     */

    //판매중 아이템 조회
    List<Store> findAllByIsActiveTrue();

    //지역 한정 아이템 조회
    List<Store> findLimitedItemsByRegionCode(int regionCode);
}
