package kr.co.breadfeetserver.application.bakery;

import static kr.co.breadfeetserver.fixture.BakeryFixture.aBakery;
import static kr.co.breadfeetserver.fixture.BakeryFixture.aBakeryCreateRequest;
import static kr.co.breadfeetserver.fixture.BakeryFixture.aBakeryUpdateRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.domain.bakery.BakeryJpaRepository;
import kr.co.breadfeetserver.presentation.bakery.dto.BakeryCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.BakeryUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("BakeryService 단위 테스트")
class BakeryServiceTest {

    @Mock
    private BakeryJpaRepository repository;

    private BakeryService bakeryService;

    @BeforeEach
    void setUp() {
        bakeryService = new BakeryService(repository);
    }

    @Test
    @DisplayName("사용자는_빵집을_생성할_수_있다")
    void 사용자는_빵집을_생성할_수_있다() {
        // Given
        long memberId = 1L;
        BakeryCreateRequest request = aBakeryCreateRequest();
        given(repository.existsByAddress_LotNumber(any())).willReturn(false);
        given(repository.save(any(Bakery.class))).willReturn(aBakery(1L, "test"));

        // When
        Long bakeryId = bakeryService.createBakery(memberId, request);

        // Then
        assertThat(bakeryId).isEqualTo(1L);
        verify(repository).save(any(Bakery.class));
    }

    @Test
    @DisplayName("사용자는_빵집을_수정할_수_있다")
    void 사용자는_빵집을_수정할_수_있다() {
        // Given
        long memberId = 1L;
        long bakeryId = 1L;
        BakeryUpdateRequest request = aBakeryUpdateRequest(bakeryId);
        Bakery bakery = aBakery(bakeryId, "test");
        given(repository.findById(bakeryId)).willReturn(Optional.of(bakery));

        // When
        bakeryService.updateBakery(memberId, bakeryId, request);

        // Then
        assertThat(bakery.getName()).isEqualTo(request.name());
        verify(repository).findById(bakeryId);
    }

    @Test
    @DisplayName("사용자는_빵집을_삭제할_수_있다")
    void 사용자는_빵집을_삭제할_수_있다() {
        // Given
        long memberId = 1L;
        long bakeryId = 1L;
        Bakery bakery = Bakery.builder().memberId(memberId).build();
        given(repository.findById(bakeryId)).willReturn(Optional.of(bakery));

        // When
        bakeryService.deleteBakery(memberId, bakeryId);

        // Then
        verify(repository).delete(bakery);
    }
}