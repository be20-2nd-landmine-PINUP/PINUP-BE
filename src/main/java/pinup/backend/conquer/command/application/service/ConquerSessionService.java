package pinup.backend.conquer.command.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.conquer.command.application.dto.ConquerEndRequest;
import pinup.backend.conquer.command.application.dto.ConquerEndResponse;
import pinup.backend.conquer.command.application.dto.ConquerStartRequest;
import pinup.backend.conquer.command.application.dto.ConquerStartResponse;
import pinup.backend.conquer.command.domain.entity.ConquerSession;
import pinup.backend.conquer.command.domain.entity.Region;
import pinup.backend.conquer.command.domain.entity.Territory;
import pinup.backend.conquer.command.domain.repository.ConquerSessionRepository;
import pinup.backend.conquer.command.domain.repository.TerritoryRepository;
import pinup.backend.conquer.query.mapper.RegionMapper;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.UserRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class ConquerSessionService {

    private final RegionMapper regionMapper;
    private final ConquerSessionRepository conquerSessionRepository;
    private final TerritoryRepository territoryRepository;
    private final UserRepository userRepository;

    private static final Duration CONQUER_DURATION = Duration.ofHours(2);

    public ConquerStartResponse startConquering(Long userId, ConquerStartRequest request) {
        Region region = regionMapper.findRegion(request.getLongitude(), request.getLatitude());
        if (region == null) {
            throw new RuntimeException("No region found for the given coordinates.");
        }

        ConquerSession session = new ConquerSession();
        session.setUserId(userId);
        session.setRegionId(region.getRegionId());
        session.setStartedAt(Instant.now());
        session.setStatus(ConquerSession.Status.RUNNING);

        ConquerSession savedSession = conquerSessionRepository.save(session);

        return new ConquerStartResponse(savedSession.getId());
    }

    public ConquerEndResponse endConquering(Long userId, ConquerEndRequest request) {
        ConquerSession session = conquerSessionRepository.findByIdAndUserId(request.getSessionId(), userId)
                .orElseThrow(() -> new RuntimeException("Conquer session not found or you don't have permission."));

        if (session.getStatus() != ConquerSession.Status.RUNNING) {
            return ConquerEndResponse.of("FAILED", "This session is not active.");
        }

        Instant now = Instant.now();
        Duration elapsed = Duration.between(session.getStartedAt(), now);

        if (elapsed.compareTo(CONQUER_DURATION) < 2L) {
            return ConquerEndResponse.of("FAILED", "The conquest requires at least 2 hours.");
        }

        Region currentRegion = regionMapper.findRegion(request.getLongitude(), request.getLatitude());
        if (currentRegion == null || !currentRegion.getRegionId().equals(session.getRegionId())) {
            session.setStatus(ConquerSession.Status.CANCELED);
            conquerSessionRepository.save(session);
            return ConquerEndResponse.of("FAILED", "You are not in the same region where you started.");
        }

        // Success
        session.setStatus(ConquerSession.Status.COMPLETED);
        session.setEndedAt(now);
        conquerSessionRepository.save(session);

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Create a new Territory record to mark the conquest
        Territory territory = new Territory(
                0L, // territoryId will be auto-generated
                user,
                currentRegion, // Link to the conquered region
                Date.from(session.getStartedAt()),
                Date.from(session.getEndedAt()),
                1, // Initial visit count
                null // photoUrl, not set for now
        );
        territoryRepository.save(territory);

        String message = String.format("Successfully conquered %s!", currentRegion.getRegionName());
        return ConquerEndResponse.of("SUCCESS", message, currentRegion);
    }
}
