package pinup.backend.store.command.domain;

import jakarta.persistence.*;
import lombok.*;
import pinup.backend.conquer.command.domain.entity.Region;
import pinup.backend.member.command.domain.Admin;
import pinup.backend.store.command.dto.StoreRequestDto;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store")    //store로 이름 변경시 필요한지 피드백 필요
@Builder
public class Store {

    // 아이템ID = PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;

    //행정구역ID = FK(관계있음) 한지역에 아이템 여러개있음
    @ManyToOne
    @JoinColumn(name = "region_id", nullable = true)
    private Region region;

    // 관리자(아이템 등록)
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    // 아이템 이름
    @Column(nullable = false, length = 50)
    private String name;

    // 아이템 설명
    @Column(nullable = false, length = 100)
    private String description;

    // 아이템 가격
    @Column(nullable = false)
    private int price;

    // 아이템 카테고리 (MARKER, SPECIALTY, BUILDING, TILE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StoreItemCategory category;

    // 한정판 속성(일반/ 한정판/ 이벤트)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StoreLimitType limitType;

    // 아이템 이미지 URL
    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    // 아이템 판매 여부
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    // 아이템 정보 수정 메서드
    public void update(StoreRequestDto dto) {
        this.region = dto.getRegion();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.price = dto.getPrice();
        this.category = dto.getCategory();
        this.limitType = dto.getLimitType();
        this.imageUrl = dto.getImageUrl();
    }

    // 아이템 판매 중지(관리자 전용)
    public void deactivate() {
        this.isActive = false;
    }

    //아이템 수정(관리자 전용)
    public void updateInfo(
            String name, String description, int price, StoreItemCategory category, StoreLimitType limitType, String imageUrl) {
    }
}
