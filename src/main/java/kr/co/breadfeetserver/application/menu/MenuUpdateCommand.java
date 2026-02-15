package kr.co.breadfeetserver.application.menu;

import java.util.List;
import kr.co.breadfeetserver.presentation.bakery.dto.request.SingleMenuUpdateRequest;

public record MenuUpdateCommand(
        Long bakeryId,
        List<SingleMenuUpdateRequest> menus
) {

    public boolean isEmpty() {
        return menus == null || menus.isEmpty();
    }

    public static MenuUpdateCommand of(Long bakeryId, List<SingleMenuUpdateRequest> menus) {
        if (menus == null) {
            return new MenuUpdateCommand(bakeryId, null);
        }
        if (menus.isEmpty()) {
            return new MenuUpdateCommand(bakeryId, null);
        }
        return new MenuUpdateCommand(bakeryId, menus);
    }
}
