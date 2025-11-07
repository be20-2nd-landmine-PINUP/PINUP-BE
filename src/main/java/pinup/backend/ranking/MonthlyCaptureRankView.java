package pinup.backend.ranking;

import java.util.Date;

// 월간 랭킹 조회 결과(프로젝션)
public interface MonthlyCaptureRankView {
    Long getUserId();            // t.userId.userId
    Long getCaptureCount();      // count(distinct region)
    Date getLastCaptureAt();     // max(capture_end_at)
}