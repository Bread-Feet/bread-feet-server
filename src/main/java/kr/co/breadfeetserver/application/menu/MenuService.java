package kr.co.breadfeetserver.application.menu;

import java.util.List;
import kr.co.breadfeetserver.domain.menu.Menu;
import kr.co.breadfeetserver.domain.menu.MenuJpaRepository;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import kr.co.breadfeetserver.infra.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {

    private final MenuJpaRepository menuJpaRepository;

    public void createMenu(MenuCreateCommand command) {
        if (command == null) {
            return;
        }

        List<Menu> menus = command.menus().stream()
                .map(singleRequest -> singleRequest.toEntity(command.bakeryId()))
                .toList();

        menuJpaRepository.saveAll(menus);
    }

    public void updateMenu(MenuUpdateCommand command) {
        if (command == null) {
            throw new BreadFeetBusinessException(ErrorCode.BAKERY_NOT_FOUND);
        }
        menuJpaRepository.deleteAllByBakeryId(command.bakeryId());

        if (!command.isEmpty()) {
            List<Menu> menus = command.menus().stream()
                    .map(singleRequest -> singleRequest.toEntity(command.bakeryId()))
                    .toList();
            menuJpaRepository.saveAll(menus);
        }
    }
}