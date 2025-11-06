package pinup.backend.store.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.command.controller.InventoryController;
import pinup.backend.store.command.domain.*;
import pinup.backend.store.command.service.InventoryService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
class
InventoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private Users testUser;
    private Store testStore;
    private Inventory testInventory;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();

        testUser = new Users();
        testUser.setUserId(1L);
        testUser.setNickname("테스터");

        testStore = Store.builder()
                .itemId(1)
                .name("테스트 배경")
                .description("테스트용 배경 아이템")
                .price(100)
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
    @DisplayName("유저 보유 아이템 조회 성공")
    void getUserInventory() throws Exception {
        when(inventoryService.getUserInventory(anyLong()))
                .thenReturn(List.of(testInventory));

        mockMvc.perform(get("/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].store.name").value("테스트 배경"))
                .andExpect(jsonPath("$[0].isEquipped").value(true))
                .andDo(print());
    }
}