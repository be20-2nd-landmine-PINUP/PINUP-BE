package pinup.backend.point.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.member.command.domain.Users;
import pinup.backend.point.command.domain.PointLog;
import pinup.backend.point.command.domain.PointSourceType;
import pinup.backend.point.command.domain.TotalPoint;
import pinup.backend.point.command.repository.PointLogRepository;
import pinup.backend.point.command.repository.TotalPointRepository;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

    private final PointLogRepository pointLogRepository;
    private final TotalPointRepository totalPointRepository;

    /**
     * ✅ 정복(CAPTURE) 완료 시 5점 지급
     */
    @Transactional
    public void grantCapturePoint(Long userId, Long regionId) {
        String eventKey = "CAPTURE_" + userId + "_" + regionId; // 중복 지급 방지용 키

        if (pointLogRepository.existsByEventKey(eventKey)) return; // 둘 다 동일 정책

        Users user = Users.builder().userId(userId).build();
        int pointValue = 5;

        // 1️⃣ 포인트 로그 저장
        PointLog log = PointLog.builder()
                .user(user)
                .pointSourceId(regionId.intValue())  // 지역 ID 저장
                .sourceType(PointSourceType.CAPTURE) // ✅ 정복 이벤트 타입
                .eventKey(eventKey)
                .pointValue(pointValue)
                .build();
        pointLogRepository.save(log);

        // 2️⃣ 누적 포인트 조회 또는 새로 생성
        TotalPoint total = totalPointRepository.findByUserId(userId)
                .orElseGet(() -> TotalPoint.builder()
                        .user(user)
                        .totalPoint(0)
                        .build());

        // 3️⃣ 포인트 증가 후 저장
        total.addPoints(pointValue);
        totalPointRepository.save(total);
    }

    /** 월간 100명 이하 지역 방문 보너스 +10점 (yyyyMM 단위로 idempotent) */
    @Transactional
    public void grantCaptureMonthlyBonus(Long userId, Long regionId, YearMonth yearMonth) {
        String ym = yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
        String eventKey = "CAPTURE_BONUS_" + ym + "_" + userId + "_" + regionId;

        if (pointLogRepository.existsByEventKey(eventKey)) {
            // 이미 지급된 보너스면 무시(중복 방지)
            return;
        }

        Users user = Users.builder().userId(userId).build();
        int bonus = 10;

        PointLog log = PointLog.builder()
                .user(user)
                .pointSourceId(regionId.intValue())
                .sourceType(PointSourceType.MONTHLY_BONUS) // 보너스는 MONTHLY_BONUS타입으로 기록
                .eventKey(eventKey)
                .pointValue(bonus)
                .build();
        pointLogRepository.save(log);

        TotalPoint total = totalPointRepository.findByUserId(userId)
                .orElseGet(() -> TotalPoint.builder().user(user).totalPoint(0).build());
        total.addPoints(bonus);
        totalPointRepository.save(total);
    }

    /**
     * ✅ 외부 서비스로부터 거래기록을 전달받아 로그 저장만 수행
     * total_point 계산은 별도 내부 스케줄러/서비스에서 관리
     */
    @Transactional
    public void recordTransaction(
            Users user,
            PointSourceType sourceType,
            Integer pointSourceId,
            int pointValue,
            String eventKey
    ) {
        if (pointLogRepository.existsByEventKey(eventKey)) {
            throw new IllegalStateException("이미 처리된 포인트 이벤트입니다: " + eventKey);
        }

        PointLog log = PointLog.builder()
                .user(user)
                .pointSourceId(pointSourceId)
                .sourceType(sourceType)
                .eventKey(eventKey)
                .pointValue(-pointValue)  // 구매이므로 음수 기록 (총포인트 차감은 포인트모듈이 계산)
                .build();

        pointLogRepository.save(log);


        // ② 유저의 total_point 조회 (없으면 새로 생성)
        TotalPoint total = totalPointRepository.findByUserId(user.getUserId())
                .orElseGet(() -> TotalPoint.builder()
                        .user(user)
                        .totalPoint(0)
                        .build());

    }

        @Transactional(readOnly = true)
        public int getUserTotalPoint (Long userId){
            return totalPointRepository.findByUserId(userId)
                    .map(TotalPoint::getTotalPoint)
                    .orElse(0); // 없으면 0 포인트로 간주
        }

    @Transactional
    public void grantLike(Long authorId, Long feedId){

        // 2️⃣ 작성자 유저 정보 조회 (엔티티 참조)
        Users author = Users.builder().userId(authorId).build();
        // ⚠️ 실제 환경에서는 userRepository.findById(authorId)
        //     로 가져오는 게 JPA 연관관계 안정성 측면에서 더 좋음.

        int pointValue = 5; // 좋아요 포인트 값

        // 3️⃣ 포인트 로그 저장 (feedId → point_source_id로 매핑)
        PointLog log = PointLog.builder()
                .user(author)
                .pointSourceId(feedId.intValue())   // ✅ feedId를 point_source_id로 저장
                .sourceType(PointSourceType.LIKE)
                .eventKey(feedId.toString())
                .pointValue(pointValue)
                .build();
        pointLogRepository.save(log);

        // 4️⃣ 누적 포인트 조회 or 새로 생성
        TotalPoint total = totalPointRepository.findByUserId(authorId)
                .orElseGet(() -> TotalPoint.builder()
                        .user(author)
                        .totalPoint(0)
                        .build());

        // 5️⃣ 포인트 증가
        total.addPoints(pointValue);

        // 6️⃣ DB 반영
        totalPointRepository.save(total);
    }
}