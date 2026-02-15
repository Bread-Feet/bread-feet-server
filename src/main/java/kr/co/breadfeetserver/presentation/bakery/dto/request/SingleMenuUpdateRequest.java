package kr.co.breadfeetserver.presentation.bakery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.co.breadfeetserver.domain.menu.Menu;
import lombok.Builder;

@Builder
public record SingleMenuUpdateRequest(
        @NotBlank(message = "메뉴 이름은 필수입니다.")
        String name,
        @NotNull(message = "가격은 필수입니다.")
        @Positive(message = "가격은 양수여야 합니다.")
        Integer price,
        String thumbnailUrl,
        boolean isRepresentation
) {

    public Menu toEntity(Long bakeryId) {
        return Menu.builder()
                .bakeryId(bakeryId)
                .name(name)
                .price(price)
                .thumbnailUrl(thumbnailUrl)
                .isRepresentation(isRepresentation)
                .build();
    }
}
