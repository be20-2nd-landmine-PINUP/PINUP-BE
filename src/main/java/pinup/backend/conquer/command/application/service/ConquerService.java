package pinup.backend.conquer.command.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pinup.backend.conquer.command.application.dto.ConquerRequest;
import pinup.backend.conquer.command.application.dto.ConquerResponse;
import pinup.backend.conquer.command.domain.entity.Region;
import pinup.backend.conquer.command.domain.entity.Territory;
import pinup.backend.conquer.command.domain.repository.TerritoryRepository;
import pinup.backend.conquer.query.mapper.RegionMapper;
import pinup.backend.member.command.domain.Users;
import pinup.backend.member.command.repository.UserRepository;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class ConquerService {

    private final RegionMapper regionMapper;
    private final TerritoryRepository territoryRepository;
    private final UserRepository userRepository;

    public ConquerResponse conquerRegion(Long userId, ConquerRequest request) {
        Region region = regionMapper.findRegion(request.getLongitude(), request.getLatitude());
        if (region == null) {
            return ConquerResponse.of("No region found at the given coordinates.");
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        boolean alreadyConquered = territoryRepository.existsByUserIdAndRegion(user, region);

        if (alreadyConquered) {
            return ConquerResponse.of("You have already conquered this region.", region);
        }

        // Create a new Territory record to mark the conquest
        Territory territory = new Territory(
                0L, // territoryId will be auto-generated
                user,
                region,
                new Date(), // captureStartAt
                new Date(), // captureEndAt
                1, // Initial visit count
                null // photoUrl
        );
        territoryRepository.save(territory);

        String message = String.format("Successfully conquered %s!", region.getRegionName());
        return ConquerResponse.of(message, region);
    }
}
