package pinup.backend.store.command.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pinup.backend.member.command.domain.Users;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    // 복합키 (user_id + item_id)
    @EmbeddedId
    private InventoryKey id;

    // 유저 정보 PFK
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    // 상점 아이템 PFK
    @ManyToOne
    @MapsId("itemId") // InventoryKey.itemId와 매핑
    @JoinColumn(name = "item_id", nullable = false)
    private Store store;

    // 아이템 획득 일시
    @Column(name = "earned_at", nullable = false)
    private LocalDateTime earnedAt;

    // 아이템 장착 상태 여부
    @Column(name = "is_equipped", nullable = false)
    private boolean isEquipped = true;

    // 인벤토리 생성 메서드
    public static Inventory create(Users userId, Store store) {
        return Inventory.builder()
                .id(new InventoryKey(userId.getUserId(), store.getItemId()))
                .users(userId)
                .store(store)
                .earnedAt(LocalDateTime.now())
                .isEquipped(true)
                .build();
    }
}