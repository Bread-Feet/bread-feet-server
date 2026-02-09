package kr.co.breadfeetserver.application.menu;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import kr.co.breadfeetserver.domain.menu.MenuJpaRepository;
import kr.co.breadfeetserver.presentation.bakery.dto.request.SingleMenuCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.SingleMenuUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import kr.co.breadfeetserver.infra.exception.BreadFeetBusinessException;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuService 단위 테스트")
class MenuServiceTest {

    @Mock
    private MenuJpaRepository menuJpaRepository;

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuJpaRepository);
    }

    @Test
    @DisplayName("메뉴를_생성할_수_있다")
    void createMenuTest() {
        // Given
        Long bakeryId = 1L;
        List<SingleMenuCreateRequest> menuRequests = List.of(
                new SingleMenuCreateRequest("빵1", 1000, "url1"),
                new SingleMenuCreateRequest("빵2", 2000, "url2")
        );
        MenuCreateCommand command = new MenuCreateCommand(bakeryId, menuRequests);

        // When
        menuService.createMenu(command);

        // Then
        verify(menuJpaRepository, times(1)).saveAll(any(List.class));
    }

    @Test
    @DisplayName("null_command로_메뉴_생성시_아무_동작하지_않는다")
    void createMenuWithNullCommandTest() {
        // Given
        MenuCreateCommand command = null;

        // When
        menuService.createMenu(command);

        // Then
        verify(menuJpaRepository, never()).saveAll(any(List.class));
    }

    @Test
    @DisplayName("기존_메뉴를_새로운_메뉴_목록으로_갱신할_수_있다")
    void updateMenuWithNewMenusTest() {
        // Given
        Long bakeryId = 1L;
        List<SingleMenuUpdateRequest> menuRequests = List.of(
                new SingleMenuUpdateRequest("새빵1", 1500, "url1"),
                new SingleMenuUpdateRequest("새빵2", 2500, "url2")
        );
        MenuUpdateCommand command = new MenuUpdateCommand(bakeryId, menuRequests);

        // When
        menuService.updateMenu(command);

        // Then
        verify(menuJpaRepository, times(1)).deleteAllByBakeryId(bakeryId);
        verify(menuJpaRepository, times(1)).saveAll(any(List.class));
    }

    @Test
    @DisplayName("메뉴_목록을_빈_리스트로_갱신하면_기존_메뉴가_모두_삭제된다")
    void updateMenuWithEmptyListTest() {
        // Given
        Long bakeryId = 1L;
        List<SingleMenuUpdateRequest> menuRequests = Collections.emptyList();
        MenuUpdateCommand command = new MenuUpdateCommand(bakeryId, menuRequests);

        // When
        menuService.updateMenu(command);

        // Then
        verify(menuJpaRepository, times(1)).deleteAllByBakeryId(bakeryId);
        verify(menuJpaRepository, never()).saveAll(any(List.class));
    }

    @Test
    @DisplayName("메뉴_목록을_null로_갱신하면_기존_메뉴가_모두_삭제된다")
    void updateMenuWithNullListTest() {
        // Given
        Long bakeryId = 1L;
        MenuUpdateCommand command = MenuUpdateCommand.of(
                bakeryId, null);

        // When
        menuService.updateMenu(command);

        // Then
        verify(menuJpaRepository, times(1)).deleteAllByBakeryId(bakeryId);
        verify(menuJpaRepository, never()).saveAll(any(List.class));
    }

    @Test
    @DisplayName("null_command로_메뉴_갱신시_BreadFeetBusinessException_발생")
    void updateMenuWithNullCommandTest() {
        // Given
        MenuUpdateCommand command = null;

        // When & Then
        assertThatThrownBy(() -> menuService.updateMenu(command))
                .isInstanceOf(BreadFeetBusinessException.class)
                .hasMessage("빵집을 찾을 수 없습니다.");

        verify(menuJpaRepository, never()).deleteAllByBakeryId(any(Long.class));
        verify(menuJpaRepository, never()).saveAll(any(List.class));
    }
}