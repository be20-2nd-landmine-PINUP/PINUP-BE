package pinup.backend.conquer.command.application.dto;

import lombok.Builder;
import lombok.Getter;
import pinup.backend.conquer.command.domain.entity.Region;

@Getter
@Builder
public class ConquerResponse {
    private final String message;
    private final String regionName;
    private final String regionDepth1;
    private final String regionDepth2;
    private final String regionDepth3;

    public static ConquerResponse of(String message, Region region) {
        return ConquerResponse.builder()
                .message(message)
                .regionName(region.getRegionName())
                .regionDepth1(region.getRegionDepth1())
                .regionDepth2(region.getRegionDepth2())
                .regionDepth3(region.getRegionDepth3())
                .build();
    }

    public static ConquerResponse of(String message) {
        return ConquerResponse.builder()
                .message(message)
                .build();
    }
}
