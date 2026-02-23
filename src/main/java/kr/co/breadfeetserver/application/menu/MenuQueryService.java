package kr.co.breadfeetserver.application.menu;

import java.util.List;
import kr.co.breadfeetserver.domain.menu.Menu;
import kr.co.breadfeetserver.domain.menu.MenuQueryRepository;
import kr.co.breadfeetserver.presentation.bakery.dto.response.MenuDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class MenuQueryService {

    private final MenuQueryRepository menuQueryRepository;

    public List<MenuDetailResponse> getMenu(Long bakeryId) {

        List<Menu> menus = menuQueryRepository.findByBakeryId(bakeryId);

        return menus.stream()
                .map(MenuDetailResponse::from)
                .toList();
    }
}