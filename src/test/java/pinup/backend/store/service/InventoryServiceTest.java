package pinup.backend.store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.command.domain.*;
import pinup.backend.store.command.repository.InventoryRepository;
import pinup.backend.store.command.service.InventoryService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

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
        testUser.setUserId(1L);
        testUser.setNickname("테스터");

        testStore = Store.builder()
                .itemId(1)
                .name("테스트 아이템")
                .description("테스트 설명")
                .price(50)
                .category(StoreItemCategory.BUILDING)
                .limitType(StoreLimitType.NORMAL)
                .imageUrl("test.png")
                .isActive(true)
                .build();

        testInventory = Inventory.builder()
                .id(new InventoryKey(1L, 1))
                .users(testUser)
                .store(testStore)
                .earnedAt(LocalDateTime.now())
                .isEquipped(true)
                .build();
    }

    @Test
    @DisplayName("아이템 중복 보유 시 예외 발생")
    void validateOwnedItemThrowsException() {
        when(inventoryRepository.existsByUsersAndStore(testUser, testStore))
                .thenReturn(true);

        assertThatThrownBy(() -> inventoryService.validateOwnedItem(testUser, testStore))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 보유 중인 아이템입니다.");

        verify(inventoryRepository, times(1)).existsByUsersAndStore(testUser, testStore);
    }

    @Test
    @DisplayName("아이템 정상 추가 성공")
    void addToInventorySuccess() {
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(testInventory);

        Inventory result = inventoryService.addToInventory(testUser, testStore);

        verify(inventoryRepository, times(1)).save(any(Inventory.class));
        assertThat(result.getStore().getName()).isEqualTo("테스트 아이템");
    }
}