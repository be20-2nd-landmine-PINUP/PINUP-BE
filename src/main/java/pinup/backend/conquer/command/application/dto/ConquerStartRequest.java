package pinup.backend.conquer.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConquerStartRequest {
    private double latitude;
    private double longitude;
}
