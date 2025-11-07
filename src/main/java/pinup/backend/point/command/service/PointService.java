package pinup.backend.point.command.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.member.command.domain.Users;
import pinup.backend.point.command.domain.PointLog;
import pinup.backend.point.command.domain.PointSourceType;
import pinup.backend.point.command.domain.TotalPoint;
import pinup.backend.point.command.repository.PointLogRepository;
import pinup.backend.point.command.repository.TotalPointRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

    private final PointLogRepository pointLogRepository;
    private final TotalPointRepository totalPointRepository;

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