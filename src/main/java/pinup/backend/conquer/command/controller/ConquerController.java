package pinup.backend.conquer.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.conquer.command.application.dto.ConquerRequest;
import pinup.backend.conquer.command.application.dto.ConquerResponse;
import pinup.backend.conquer.command.application.service.ConquerService;

@RestController
@RequestMapping("/conquer")
@RequiredArgsConstructor
public class ConquerController {

    private final ConquerService conquerService;

    @PostMapping
    public ResponseEntity<ConquerResponse> conquer(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ConquerRequest request) {
        // Note: You need to have a way to get your custom User object from UserDetails.
        // For now, we'll pass a placeholder user ID.
        // Long userId = ((CustomUserDetails) userDetails).getId();
        Long userId = 1L; // Placeholder
        ConquerResponse response = conquerService.conquerRegion(userId, request);
        return ResponseEntity.ok(response);
    }
}
