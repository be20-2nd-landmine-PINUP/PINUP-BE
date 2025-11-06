package pinup.backend.store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.command.domain.*;
import pinup.backend.store.command.repository.StoreRepository;
import pinup.backend.store.command.service.InventoryService;
import pinup.backend.store.command.service.StoreService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private StoreService storeService;

    private Users testUser;
    private Store testItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new Users();
        testUser.setUserId(1L);
        testUser.setNickname("tester");

        testItem = Store.builder()
                .itemId(1)
                .name("한정판 배경")
                .description("서울 지역 한정판 아이템")
                .price(100)
                .category(StoreItemCategory.BUILDING)
                .limitType(StoreLimitType.LIMITED)
                .imageUrl("image.png")
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("판매 중인 아이템 단일 조회 성공")
    void getItemById() {
        when(storeRepository.findById(1)).thenReturn(Optional.of(testItem));

        Store result = storeService.purchaseItem(testUser, 1).getStore();

        verify(inventoryService, times(1)).validateOwnedItem(any(Users.class), any(Store.class));
        assertThat(result.getName()).isEqualTo("한정판 배경");
    }

    @Test
    @DisplayName("아이템 구매 시 인벤토리 등록 성공")
    void purchaseItem_addToInventory() {
        when(storeRepository.findById(1)).thenReturn(Optional.of(testItem));

        Inventory fakeInventory = Inventory.create(testUser, testItem);
        when(inventoryService.addToInventory(any(Users.class), any(Store.class)))
                .thenReturn(fakeInventory);

        Inventory result = storeService.purchaseItem(testUser, 1);

        verify(inventoryService, times(1)).validateOwnedItem(testUser, testItem);
        verify(inventoryService, times(1)).addToInventory(testUser, testItem);
        assertThat(result.getStore().getName()).isEqualTo("한정판 배경");
    }
}