package pinup.backend.store.command.domain;

import lombok.Getter;

@Getter
public enum StoreItemCategory {

    MARKER("MK", "지도 마커"),
    SPECIALTY("SP", "특산물"),
    BUILDING("BD","건물"),
    TILE("TL","타일보드");

    private final String code;              // 코드 값 (DB나 API용)
    private final String description;       // 한글 설명 (UI 표시용)

    // 생성자
    StoreItemCategory(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
