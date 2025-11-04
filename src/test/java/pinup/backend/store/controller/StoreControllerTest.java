package pinup.backend.store.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pinup.backend.member.command.domain.Users;
import pinup.backend.store.command.controller.StoreController;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.domain.Store;
import pinup.backend.store.command.domain.StoreItemCategory;
import pinup.backend.store.command.service.InventoryService;
import pinup.backend.store.command.service.StoreService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebMvcTest(StoreController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StoreService storeService;

    @MockitoBean
    private InventoryService inventoryService;

    private Users testUser;
    private Store testStore;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testStore = Store.builder()
                .itemId(1)
                .name("테스트 배경")
                .description("테스트용 배경 아이템")
                .price(100)
                .category(StoreItemCategory.BUILDING) // ENUM 사용
                .imageUrl("test.png")
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("전체 판애 아이템 조회 성공")
    void getActiveItems() throws Exception {
        when(storeService.getActiveItems()).thenReturn(List.of(testStore));

        mockMvc.perform(get("/store/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("테스트 배경")) // ✅ JSON 응답 검증
                .andDo(print());                              // 콘솔에 요청/응답 로그 출력

        verify(storeService, times(1)).getActiveItems(); // 서비스 메서드 호출 검증
    }

    @Test
    @DisplayName("단일 아이템 상세 조회 성공")
    void getItemDetail() throws Exception {
        // given
        when(storeService.getItemById(1)).thenReturn(testStore);

        // when + then
        mockMvc.perform(get("/store/items/1"))                 //  GET /store/items/1
                .andExpect(status().isOk())                   //  응답 코드 200 기대
                .andExpect(jsonPath("$.name").value("테스트 배경")) //  JSON 필드 값 검증
                .andDo(print());                              //  요청/응답 로그 출력

        verify(storeService, times(1)).getItemById(1);        //  서비스 메서드 호출 검증
    }

    @Test
    @DisplayName("아이템 구매 성공 (인벤토리 자동 추가)")
    void purchaseItem() throws Exception {
        // given
        Inventory inventory = Inventory.create(testUser, testStore);
        when(storeService.purchaseItem(testUser, 1)).thenReturn(inventory);

        // when + then
        mockMvc.perform(post("/store/purchase/1")              //  POST /store/purchase/1
                        .requestAttr("user", testUser))        //  인증된 유저 시뮬레이션 (@RequestAttribute("user"))
                .andExpect(status().isOk())                    // 응답 코드 200 기대
                .andExpect(jsonPath("$.store.name").value("테스트 배경")) //  응답 JSON 내부 Store.name 필드 검증
                .andDo(print());                               //  요청/응답 로그 출력

        verify(storeService, times(1)).purchaseItem(testUser, 1); //  서비스 호출 검증
    }

        }

