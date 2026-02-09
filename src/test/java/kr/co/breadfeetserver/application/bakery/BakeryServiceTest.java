package kr.co.breadfeetserver.application.bakery;

import static kr.co.breadfeetserver.fixture.AddressFixture.address;
import static kr.co.breadfeetserver.fixture.BakeryFixture.aBakery;
import static kr.co.breadfeetserver.fixture.BakeryFixture.aBakeryCreateRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import kr.co.breadfeetserver.application.menu.MenuService;
import kr.co.breadfeetserver.domain.bakery.Bakery;
import kr.co.breadfeetserver.domain.bakery.BakeryJpaRepository;
import kr.co.breadfeetserver.presentation.bakery.dto.request.AddressUpdateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryCreateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.BakeryUpdateRequest;
import kr.co.breadfeetserver.presentation.bakery.dto.request.SingleMenuUpdateRequest;
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

    @Mock
    private MenuService menuService;

    private BakeryService bakeryService;

    @BeforeEach
    void setUp() {
        bakeryService = new BakeryService(repository, menuService);
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
        bakeryService.createBakery(memberId, request);

        // Then
        verify(repository).save(any(Bakery.class));
    }

    @Test
    @DisplayName("사용자는_빵집을_수정할_수_있다")
    void 사용자는_빵집을_수정할_수_있다() {
        // Given
        long memberId = 1L;
        long bakeryId = 1L;
        // Use a modified request to ensure changes are distinct
        BakeryUpdateRequest request = new BakeryUpdateRequest(
                bakeryId,
                "Updated Bakery Name",
                new AddressUpdateRequest("Updated Road Address", "Updated Lot Number",
                        "Updated Detail"),
                "https://updated.image.url/bakery.jpg",
                "010-9876-5432",
                "10:00 - 22:00",
                "New Best Bread",
                List.of(new SingleMenuUpdateRequest("Updated Menu", 9999, "updated.menu.url"))
        );

        Bakery bakery = Bakery.builder()
                .id(bakeryId)
                .name("Old Bakery Name")
                .address(address())
                .imageUrl("https://old.image.url/bakery.jpg")
                .phoneNumber("010-1111-2222")
                .businessHours("08:00 - 20:00")
                .bestBread("Old Best Bread")
                .memberId(memberId)
                .build();

        given(repository.findById(bakeryId)).willReturn(Optional.of(bakery));

        // When
        bakeryService.updateBakery(memberId, request);
        
        // Then
        assertThat(bakery.getName()).isEqualTo(request.name());
        assertThat(bakery.getImageUrl()).isEqualTo(request.imageUrl());
        assertThat(bakery.getPhoneNumber()).isEqualTo(request.phoneNumber());
        assertThat(bakery.getBusinessHours()).isEqualTo(request.businessHours());
        assertThat(bakery.getBestBread()).isEqualTo(request.bestBread());
        assertThat(bakery.getAddress().getRoadAddress()).isEqualTo(request.address().roadAddress());
        assertThat(bakery.getAddress().getLotNumber()).isEqualTo(request.address().lotNumber());
        assertThat(bakery.getAddress().getDetail()).isEqualTo(request.address().detail());

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