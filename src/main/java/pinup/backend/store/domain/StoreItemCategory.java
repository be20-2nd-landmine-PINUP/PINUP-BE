package pinup.backend.store.domain;

import lombok.Getter;

@Getter
public enum StoreItemCategory {

    BACKGROUND("BG", "배경 아이템"),   //
    MARKER("MK", "지도 마커"),
    BORDER("BR", "프로필 테두리");    //

    private final String code;              // 코드 값 (DB나 API용)
    private final String description;       // 한글 설명 (UI 표시용)

    // 생성자
    StoreItemCategory(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
