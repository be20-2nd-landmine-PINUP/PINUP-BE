package pinup.backend.point.command.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pinup.backend.point.command.dto.GrantPointRequest;
import pinup.backend.point.command.dto.UsePointRequest;
import pinup.backend.point.command.service.PointCommandService;

@RestController
@RequestMapping("/api/points")
public class PointCommandController {

    private final PointCommandService service;

    public PointCommandController(PointCommandService service) {
        this.service = service;
    }

    // 적립 (피드/영토 등), requestbody로 json에서 DTO로 자동 매핑, 혹은 그 역 순으로.
    @PostMapping("/grant")
    public ResponseEntity<Void> grant(@Valid @RequestBody GrantPointRequest req) {
        service.grant(req.userId(), req.pointValue(), req.sourceId(), req.sourceType());
        return ResponseEntity.noContent().build();
    }

    // 차감 (스토어)
    @PostMapping("/use")
    public ResponseEntity<Void> use(@Valid @RequestBody UsePointRequest req) {
        service.use(req.userId(), req.pointValue(), req.sourceId());
        return ResponseEntity.noContent().build();
    }
}
