package pinup.backend.store.controller;


import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.command.controller.InventoryController;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.domain.InventoryKey;
import pinup.backend.store.command.domain.Store;
import pinup.backend.store.command.domain.StoreItemCategory;
import pinup.backend.store.command.service.InventoryService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class InventoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Users testUser;
    private Store testStore;
    private Inventory testInventory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();

        testUser = new Users();
        testStore = Store.builder()
                .itemId(1)
                .name("테스트 배경")
                .description("테스트용 배경 아이템")
                .price(100)
                .category(StoreItemCategory.BUILDING)
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
    @DisplayName("보유 아이템 조회 성공")
    void getUserInventory() throws Exception {
        when(inventoryService.getUserInventory(any(Users.class)))
                .thenReturn(List.of(testInventory));

        mockMvc.perform(get("/inventory")
                        .requestAttr("user", testUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("테스트 배경"))
                .andDo(print());

        verify(inventoryService, times(1)).getUserInventory(any(Users.class));
    }

    /*
    @Test
    @DisplayName("아이템 장착 성공")
    void equipItem() throws Exception {
        when(inventoryService.getEquippedItems(any(Users.class)))
                .thenReturn(List.of(testInventory));

        mockMvc.perform(post("/inventory")
                        .requestAttr("user", testUser))
                .andDo(print())
                .andExpect(status().isOk());

        verify(inventoryService, times(1))
                .getEquippedItems(any(Users.class));
    }*/
}
