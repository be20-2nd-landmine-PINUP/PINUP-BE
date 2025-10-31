package pinup.backend.ranking.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pinup.backend.ranking.command.service.RankingCommandService;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/rankings/command")
@RequiredArgsConstructor
public class RankingCommandController {

    private final RankingCommandService commandService;

    @PostMapping("/monthly/build")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> build(@RequestParam String ym) {
        validateYm(ym);
        commandService.buildMonthlyRanking(YearMonth.parse(ym));
        return ResponseEntity.accepted().build();
    }

    private static void validateYm(String ym) {
        if (ym == null || !ym.matches("^\\d{4}-\\d{2}$")) {
            throw new IllegalArgumentException("ym 형식은 YYYY-MM 입니다.");
        }
        int year = Integer.parseInt(ym.substring(0, 4));
        int month = Integer.parseInt(ym.substring(5, 7));
        if (year < 2000 || year > 2099 || month < 1 || month > 12) {
            throw new IllegalArgumentException("유효하지 않은 ym 값입니다.");
        }
    }
}
