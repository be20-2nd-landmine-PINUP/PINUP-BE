package pinup.backend.store.enums;

import lombok.Getter;

@Getter
public enum StoreItemCategory {

    BACKGROUD("BG", "배경 아이템"),
    MARKER("MK", "지도 마커"),
    BORDER("BR", "프로필 테투리");

    private final String code;              //코드 값
    private final String description;       //한글 설명



}
