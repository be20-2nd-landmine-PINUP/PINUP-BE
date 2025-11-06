package pinup.backend.point.command.controller;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pinup.backend.point.command.dto.GrantPointRequest;
import pinup.backend.point.command.dto.UsePointRequest;
import pinup.backend.point.command.service.PointCommandService;

import static pinup.backend.point.command.dto.PointSourceTypeDto.*;


@RestController
@RequestMapping("/api/points")
public class PointCommandController {

    private final PointCommandService service;
    public PointCommandController(PointCommandService service) { this.service = service; }

    // FEED 전용 (좋아요 적립)
    @PostMapping("/grant/like")
    public ResponseEntity<Void> grantLike(@Valid @RequestBody GrantPointRequest req) {
        service.grantLike(req.userId(), req.sourceId());
        return ResponseEntity.noContent().build();
    }

    // CAPTURE 전용 (점령 적립)
    @PostMapping("/grant/capture")
    public ResponseEntity<Void> grantCapture(@Valid @RequestBody GrantPointRequest req) {
        service.grantCapture(req.userId(), req.sourceId());
        return ResponseEntity.noContent().build();
    }

    // STORE 전용 (차감)
    @PostMapping("/use")
    public ResponseEntity<Void> use(@Valid @RequestBody UsePointRequest req) {
        service.use(req.userId(), req.pointValue(), req.sourceId());
        return ResponseEntity.noContent().build();
    }
}

