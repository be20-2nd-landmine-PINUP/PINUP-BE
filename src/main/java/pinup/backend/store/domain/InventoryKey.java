package pinup.backend.store.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode  //(엔티티 동일성 비교)
public class InventoryKey implements Serializable {
    /**
     *  Inventory의 복합키(Composite Key)를 정의하는 클래스
     * - user_id + store_id 조합이 기본키 역할을 함
     */

    private Long userId;     //FK to User
    private Integer itemId;     //FK to Store
}
