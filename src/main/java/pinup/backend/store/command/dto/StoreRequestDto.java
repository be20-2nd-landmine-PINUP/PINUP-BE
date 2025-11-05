package pinup.backend.store.command.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pinup.backend.conquer.command.domain.entity.Region;
import pinup.backend.store.command.domain.StoreItemCategory;
import pinup.backend.store.command.domain.StoreLimitType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequestDto {

    private Long adminId;
    private Region region;
    private String name;
    private String description;
    private int price;
    private StoreItemCategory category;
    private StoreLimitType limitType;
    private String imageUrl;

}
