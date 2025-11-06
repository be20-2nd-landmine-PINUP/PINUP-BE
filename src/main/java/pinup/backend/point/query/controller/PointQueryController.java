package pinup.backend.point.query.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pinup.backend.point.query.dto.PointBalanceResponse;
import pinup.backend.point.query.dto.PointLogResponse;
import pinup.backend.point.query.service.PointQueryService;

import java.util.List;
// 포인트 조회(read) 컨트롤러
// restcontroller: json 응답 리턴
// RequestMapping("/api/points") : URL 앞부분(prefix)이 /api/points로 고정

@RestController
@RequestMapping("/api/points")
public class PointQueryController {
    // 생성자; 의존성 주입. PointQueryService  불러서 데이터 가져옴.
    private final PointQueryService service;
    // 포인트 잔액 조회. 특정 유저의 현재 포인트 합계 조회
    public PointQueryController(PointQueryService service) {
        this.service = service;
    }
    // @GetMapping("/{userId}"): http get 요청. 경로 파라미터{userId} 받음
    // PathVariable Long userId : URL의 {userId} 부분을 자바 변수로 받음
    // responseentity.ok : 상태코드 200 + json 응답
    @GetMapping("/{userId}")
    public ResponseEntity<PointBalanceResponse> balance(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getBalance(userId));
    }

    // 포인트 로그 조회; 특정 사용자의 거래 내역을 리스트 형태로 조회
    @GetMapping("/{userId}/logs")
    public ResponseEntity<List<PointLogResponse>> logs(@PathVariable Long userId,
                                                       @RequestParam(defaultValue = "20") int limit,
                                                       @RequestParam(defaultValue = "0") int offset) {
        return ResponseEntity.ok(service.getLogs(userId, limit, offset));
    }
    // limit은 한번에 가져올 데이터 개수 (페이징 처리)
    // offset은 몇번째부터 가져올지. 건너뛰기 수.
    // defaultvalue는 요청에 값이 없을 때 기본으로 쓸 값

}
//@RequestParam(defaultValue="20") int limit 은
//“URL 뒤에 ?limit=10이 붙으면 10으로 받고, 없으면 20으로 처리하라”는 뜻
