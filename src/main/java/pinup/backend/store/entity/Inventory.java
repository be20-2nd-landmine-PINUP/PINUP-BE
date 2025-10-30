package pinup.backend.store.entity;

import groovy.transform.builder.Builder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @EmbeddedId
    private InventoryKey id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Store store;

    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @Column(name = "earned_at", nullable = false)
    private LocalDateTime earnedAt;

    @Column(name = "is_equipped", nullable = false)
    private boolean isEquipped = true;

}
