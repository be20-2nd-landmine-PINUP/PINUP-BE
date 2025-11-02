package pinup.backend.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pinup.backend.store.domain.Store;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    //판매중 아이템 조회
    List<Store> findAllByIsActiveTrue();

    List<Store> findByRegionRegionId(Long regionRegionId);
}
