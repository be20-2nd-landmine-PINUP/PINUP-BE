package pinup.backend.store.StoreServiceTest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pinup.backend.member.command.domain.Users;
import pinup.backend.point.command.domain.PointSourceType;
import pinup.backend.point.command.service.PointService;
import pinup.backend.store.command.domain.*;
import pinup.backend.store.command.repository.StoreRepository;
import pinup.backend.store.command.service.InventoryService;
import pinup.backend.store.command.service.StoreService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private PointService pointService; //

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
    @DisplayName("✅ 아이템 구매 시 포인트 잔액 확인 후 인벤토리 등록 성공")
    void purchaseItem_addToInventory_success() {
        // given
        when(storeRepository.findById(1)).thenReturn(Optional.of(testItem));

        Inventory fakeInventory = Inventory.create(testUser, testItem);
        when(inventoryService.addToInventory(any(Users.class), any(Store.class)))
                .thenReturn(fakeInventory);

        // ✅ 포인트 잔액은 500으로 가정 (충분함)
        when(pointService.getUserTotalPoint(testUser.getUserId())).thenReturn(500);

        // when
        Inventory result = storeService.purchaseItem(testUser, 1);

        // then
        verify(inventoryService, times(1)).validateOwnedItem(testUser, testItem);
        verify(inventoryService, times(1)).addToInventory(testUser, testItem);

        // ✅ 포인트 모듈 호출 확인
        verify(pointService, times(1)).recordTransaction(
                eq(testUser),
                eq(PointSourceType.STORE),
                eq(testItem.getItemId()),
                eq(testItem.getPrice()),
                anyString()
        );

        assertThat(result.getStore().getName()).isEqualTo("한정판 배경");
    }

    @Test
    @DisplayName("❌ 포인트 부족 시 구매 실패 (IllegalStateException 발생)")
    void purchaseItem_insufficientPoint_fail() {
        // given
        when(storeRepository.findById(1)).thenReturn(Optional.of(testItem));
        when(pointService.getUserTotalPoint(testUser.getUserId())).thenReturn(50); // ❌ 포인트 부족

        // when & then
        try {
            storeService.purchaseItem(testUser, 1);
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).contains("보유 포인트가 부족합니다");
        }

        // ✅ 포인트 부족 시 인벤토리/포인트 모듈 호출 안 됨
        verify(inventoryService, never()).addToInventory(any(), any());
        verify(pointService, never()).recordTransaction(any(), any(), any(), anyInt(), anyString());
    }

    @Test
    @DisplayName("✅ 판매 중인 아이템 단일 조회 후 구매 성공 흐름 검증")
    void purchaseItem_validFlow() {
        // given
        when(storeRepository.findById(1)).thenReturn(Optional.of(testItem));
        when(pointService.getUserTotalPoint(anyLong())).thenReturn(200);
        when(inventoryService.addToInventory(any(), any())).thenReturn(Inventory.create(testUser, testItem));

        // when
        Inventory result = storeService.purchaseItem(testUser, 1);

        // then
        verify(storeRepository, times(1)).findById(1);
        verify(pointService, times(1)).recordTransaction(any(), any(), any(), anyInt(), anyString());
        assertThat(result.getStore().getName()).isEqualTo("한정판 배경");
    }
}