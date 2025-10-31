package pinup.backend.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "store")    //store로 이름 변경시 필요한지 피드백 필요
public class Store {

    // 아이템ID = PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int itemId;

    // 행정구역ID = FK(관계있음) 한지역에 아이템 여러개있음
    // @ManyToOne
    //@JoinColumn(name = "reion_code", nullable = true)
    //private region region;

    // 아이템 이름
    @Column(nullable = false, length = 50)
    private String name;

    // 아이템 설명
    @Column(nullable = false, length = 100)
    private String description;

    // 아이템 가격
    @Column(nullable = false)
    private int price;

    // 아이템 카테고리 (Enum)
    @Enumerated
    @Column(nullable = false, length = 20)
    private StoreItemCategory category;

    // 아이템 이미지 경로
    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    // 첫 생성시 판매 중
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    // 판매중지 메소드
    public void deactivate() {
        this.isActive = false;
    }
}
