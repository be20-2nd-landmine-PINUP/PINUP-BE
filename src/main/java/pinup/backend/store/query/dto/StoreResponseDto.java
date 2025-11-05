package pinup.backend.store.query.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponseDto {
    private Integer itemId;
    private String name;
    private String description;
    private Integer price;
    private String category;
    private String limitType;
    private String imageUrl;
    private Boolean isActive;
    private String regionName;
}
