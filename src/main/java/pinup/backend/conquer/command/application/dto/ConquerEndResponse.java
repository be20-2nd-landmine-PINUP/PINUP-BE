package pinup.backend.conquer.command.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import pinup.backend.conquer.command.domain.entity.Region;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Don't include null fields in the JSON output
public class ConquerEndResponse {
    private final String status;
    private final String message;
    private final String regionName;
    private final String regionDepth1;
    private final String regionDepth2;
    private final String regionDepth3;

    public static ConquerEndResponse of(String status, String message, Region region) {
        return ConquerEndResponse.builder()
                .status(status)
                .message(message)
                .regionName(region.getRegionName())
                .regionDepth1(region.getRegionDepth1())
                .regionDepth2(region.getRegionDepth2())
                .regionDepth3(region.getRegionDepth3())
                .build();
    }

    public static ConquerEndResponse of(String status, String message) {
        return ConquerEndResponse.builder()
                .status(status)
                .message(message)
                .build();
    }
}
