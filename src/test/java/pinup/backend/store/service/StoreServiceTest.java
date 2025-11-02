package pinup.backend.store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.domain.Store;
import pinup.backend.store.domain.StoreItemCategory;
import pinup.backend.store.repository.StoreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private StoreService storeService;

    private Store testItem;
    private Users testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new Users();
        testItem = Store.builder()
                .itemId(1)
                .name("한정판 배경")
                .description("서울 지역 한정판 아이템")
                .price(100)
                .category(StoreItemCategory.BACKGROUND)
                .imageUrl("image.png")
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("판매 중인 아이템 전체 조회 성공")
    void getActiveItems() {
        when(storeRepository.findAllByIsActiveTrue()).thenReturn(List.of(testItem));

        List<Store> result = storeService.getActiveItems();

        assertThat(result.get(0).getName()).isEqualTo("한정판 배경");
        verify(storeRepository, times(1)).findAllByIsActiveTrue();
    }

    @Test
    @DisplayName("단일 아이템 상세 조회 성공")
    void getItemById() {
        when(storeRepository.findById(1)).thenReturn(Optional.of(testItem));
        {

            Store result = storeService.getItemById(1);

            assertThat(result.getDescription()).contains("서울 지역");
            verify(storeRepository, times(1)).findById(1);


        }
    }
}
