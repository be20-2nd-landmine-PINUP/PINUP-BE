package pinup.backend.store.command.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSummaryResponseDto {
    private Integer itemId;
    private String name;
    private Integer price;
    private String category;
    private String imageUrl;
    private boolean isLimited;
}
