package kr.co.breadfeetserver.presentation.bakery.dto.response;

import kr.co.breadfeetserver.domain.menu.Menu;

public record MenuDetailResponse(
        String name,
        Integer price,
        String thumbnailUrl
) {

    public static MenuDetailResponse from(Menu menu) {
        return new MenuDetailResponse(
                menu.getName(),
                menu.getPrice(),
                menu.getThumbnailUrl()
        );
    }
}
