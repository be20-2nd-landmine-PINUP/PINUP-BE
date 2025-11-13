package pinup.backend.point.query;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.point.command.domain.PointLogDto;

import java.util.List;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointQueryController {

    private final PointQueryService pointQueryService;

    // 1) 유저 누적 포인트
    @GetMapping("/total")
    public int getUserTotalPoint(@RequestParam Long userId) {
        return pointQueryService.getUserTotalPoint(userId);
    }

    // 2) 유저 포인트 로그
    @GetMapping("/logs")
    public List<PointLogDto> getUserPointLogs(@RequestParam Long userId) {
        return pointQueryService.getUserPointLogs(userId)
                .stream()
                .map(PointLogDto::from)
                .toList();
    }
}
