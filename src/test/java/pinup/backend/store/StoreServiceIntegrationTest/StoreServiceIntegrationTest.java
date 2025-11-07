package pinup.backend.store.StoreServiceIntegrationTest;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pinup.backend.member.command.domain.Admin;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.AdminRepository;
import pinup.backend.member.command.repository.UserRepository;
import pinup.backend.point.command.domain.TotalPoint;
import pinup.backend.point.command.repository.PointLogRepository;
import pinup.backend.point.command.repository.TotalPointRepository;
import pinup.backend.store.command.domain.Inventory;
import pinup.backend.store.command.domain.Store;
import pinup.backend.store.command.domain.StoreItemCategory;
import pinup.backend.store.command.domain.StoreLimitType;
import pinup.backend.store.command.repository.StoreRepository;
import pinup.backend.store.command.service.StoreService;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional  // ✅ 반드시 유지
public class StoreServiceIntegrationTest {

    @Autowired private StoreService storeService;
    @Autowired private StoreRepository storeRepository;
    @Autowired private PointLogRepository pointLogRepository;
    @Autowired private TotalPointRepository totalPointRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("✅ 실제 DB에 포인트 차감 및 로그가 반영되는지 검증")
    void purchaseItem_db() {
        // given
        Users user = userRepository.save(
                Users.builder()
                        .loginType(Users.LoginType.GOOGLE)
                        .name("테스트 유저")
                        .email("tester@example.com")
                        .nickname("tester")
                        .gender(Users.Gender.U)
                        .birthDate(LocalDate.of(2000, 1, 1))
                        .preferredCategory(Users.PreferredCategory.자연)
                        .preferredSeason(Users.PreferredSeason.봄)
                        .status(Users.Status.ACTIVE)
                        .build()
        );

        totalPointRepository.save(
                TotalPoint.builder()
                        .totalPoint(500)
                        .user(user)
                        .build()
        );

        Admin admin = adminRepository.save(
                Admin.builder()
                        .name("테스트 관리자")
                        .password("1234")
                        .status(Admin.Status.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        Store store = storeRepository.save(
                Store.builder()
                        .admin(admin)
                        .name("테스트 배경")
                        .description("테스트용 아이템입니다.")
                        .price(100)
                        .category(StoreItemCategory.BUILDING)
                        .imageUrl("https://example.com/test.png")
                        .isActive(true)
                        .limitType(StoreLimitType.NORMAL)
                        .build()
        );

        // when
        Inventory inventory = storeService.purchaseItem(user, store.getItemId());

        // then
        assertThat(inventory).isNotNull();
        assertThat(inventory.getStore().getName()).isEqualTo("테스트 배경");
        assertThat(pointLogRepository.count()).isGreaterThan(0);

        TotalPoint updated = totalPointRepository.findByUserId(user.getUserId()).orElseThrow();
        assertThat(updated.getTotalPoint()).isEqualTo(400);
    }
}