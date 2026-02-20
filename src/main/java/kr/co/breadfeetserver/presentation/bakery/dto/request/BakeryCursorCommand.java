package kr.co.breadfeetserver.presentation.bakery.dto.request;

import jakarta.validation.constraints.Min;
import kr.co.breadfeetserver.presentation.annotation.CursorSize;

public record BakeryCursorCommand(
        @Min(0)
        Long cursor,
        @CursorSize
        int size,
        String keyword,
        Long memberId
) {

}
