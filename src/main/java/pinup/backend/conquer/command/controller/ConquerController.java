package pinup.backend.conquer.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.conquer.command.application.dto.ConquerEndRequest;
import pinup.backend.conquer.command.application.dto.ConquerEndResponse;
import pinup.backend.conquer.command.application.dto.ConquerStartRequest;
import pinup.backend.conquer.command.application.dto.ConquerStartResponse;
import pinup.backend.conquer.command.application.service.ConquerSessionService;

@RestController
@RequestMapping("/conquer")
@RequiredArgsConstructor
public class ConquerController {

    private final ConquerSessionService conquerSessionService;

    @PostMapping("/start")
    public ResponseEntity<ConquerStartResponse> startConquering(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ConquerStartRequest request) {
        // Note: You need to have a way to get your custom User object from UserDetails.
        // For now, we'll pass a placeholder user ID.
        // Long userId = ((CustomUserDetails) userDetails).getId();
        Long userId = 1L; // Placeholder
        ConquerStartResponse response = conquerSessionService.startConquering(userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/end")
    public ResponseEntity<ConquerEndResponse> endConquering(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ConquerEndRequest request) {
        // Long userId = ((CustomUserDetails) userDetails).getId();
        Long userId = 1L; // Placeholder
        ConquerEndResponse response = conquerSessionService.endConquering(userId, request);
        return ResponseEntity.ok(response);
    }
}
