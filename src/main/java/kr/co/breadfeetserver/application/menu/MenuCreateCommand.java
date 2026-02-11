package kr.co.breadfeetserver.application.menu;

import java.util.List;
import kr.co.breadfeetserver.presentation.bakery.dto.request.SingleMenuCreateRequest;

public record MenuCreateCommand(
        Long bakeryId,
        List<SingleMenuCreateRequest> menus
) {

    public static MenuCreateCommand of(Long bakeryId, List<SingleMenuCreateRequest> menus) {
        if (menus.isEmpty()) {
            return null;
        }
        return new MenuCreateCommand(bakeryId, menus);
    }
}
