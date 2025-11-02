package pinup.backend.store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.domain.Inventory;
import pinup.backend.store.domain.InventoryKey;
import pinup.backend.store.domain.Store;
import pinup.backend.store.domain.StoreItemCategory;
import pinup.backend.store.repository.InventoryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Users testUser;
    private Store testStore;
    private Inventory testInventory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new Users();
        testStore = Store.builder()
                .itemId(1)
                .name("테스트 아이템")
                .description("테스트 설명")
                .price(50)
                .category(StoreItemCategory.BACKGROUND)
                .imageUrl("test.png")
                .isActive(true)
                .build();

        testInventory = Inventory.builder()
                .id(new InventoryKey(1L, 1))
                .userId(testUser)
                .store(testStore)
                .isEquipped(true)
                .build();
    }

    @Test
    @DisplayName("유저 보유 아이템 조회 성공")
    void getUserInventory() {
        when(inventoryRepository.findAllByUserId(testUser))
                .thenReturn(List.of(testInventory));

        List<Inventory> result = inventoryService.getUserInventory(testUser);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStore().getName()).isEqualTo("테스트 아이템");
        verify(inventoryRepository, times(1)).findAllByUserId(testUser);
    }

    @Test
    @DisplayName("이미 보유 중인 아이템입니다.")
    void vailddateOwnedItem() {
        when(inventoryRepository.existsByUserIdAndStore(testUser, testStore))
                .thenReturn(true);

        assertThatThrownBy(() ->
                inventoryService.validateOwnedItem(testUser, testStore)
        ).isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 보유 중인 아이템입니다.");

        verify(inventoryRepository, times(1)).existsByUserIdAndStore(testUser, testStore);
    }

    @Test
    @DisplayName("새 아이템 인벤토리에 정상 추가")
    void addToInventory() {
        when(inventoryRepository.save(any(Inventory.class)))
                .thenReturn(testInventory);

        Inventory result = inventoryService.addToInventory(testUser, testStore);

        assertThat(result.getStore().getName()).isEqualTo("테스트 아이템");
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }


}
