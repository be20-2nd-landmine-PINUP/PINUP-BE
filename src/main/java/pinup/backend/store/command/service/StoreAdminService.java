package pinup.backend.store.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.conquer.command.domain.entity.Region;
import pinup.backend.member.command.domain.Admin;
import pinup.backend.member.command.repository.AdminRepository;
import pinup.backend.store.command.domain.Store;
import pinup.backend.store.command.dto.StoreRequestDto;
import pinup.backend.store.command.repository.RegionRepository;
import pinup.backend.store.command.repository.StoreRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreAdminService {

    private final StoreRepository storeRepository;
    private final AdminRepository adminRepository;
    private final RegionRepository regionRepository;

    // 아이템 등록 (관리자 전용)
    public Store registerItem(Long adminId, StoreRequestDto dto) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없습니다."));

        Region region = resolveRegion(dto.getRegionId());

        Store store = Store.builder()
                .admin(admin)
                .region(region)
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .limitType(dto.getLimitType())
                .imageUrl(dto.getImageUrl())
                .isActive(true)
                .build();

        return storeRepository.save(store);
    }

    // 아이템 수정 (관리자 전용)
    public Store updateItem(Integer itemId, StoreRequestDto dto) {
        Store store = storeRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템을 찾을 수 없습니다."));

        Region region = resolveRegion(dto.getRegionId());
        return storeRepository.save(store);
    }

    //아이템 삭제 ( 관리자 전용)
    public void deleteItem(Integer itemId) {
        Store store = storeRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템을 찾을 수 없습니다."));
        storeRepository.delete(store);
    }

    private Region resolveRegion(Long regionId) {
        if (regionId == null) {
            return null;
        }
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("행정구역을 찾을 수 없습니다."));
    }

}
