package pinup.backend.ranking.command.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
//복합키를 정의하는 클래스
// year_month + user_id 가 합쳐져서 유일한 키가 된다.
@Embeddable
//@Embeddable 클래스는 별도의 테이블이 생성되지 않고,
//해당 엔티티의 컬럼으로 내부에 포함(Embed) 됩니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Serializable은 JPA에서 복합키 클래스에 필수로 구현해야 하는 인터페이스
public class UserMonthlyTerritoryRankKey implements Serializable {
    //"YYYY-MM" 형태의 연월, 예: "2025-10"
    @Column(name = "year_month", length = 7, nullable = false)
    private String yearMonth; // 예: "2025-10"
    // 사용자 고유 id
    @Column(name = "user_id", nullable = false)
    private Long userId;
}
